DESCRIPTION = "Package to create a rootfs.cpio using docker container"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

INSANE_SKIP_${PN} += "already-stripped"
SKIP_FILEDEPS = "1"
EXCLUDE_FROM_SHLIBS = "1"

PACKAGE_ARCH = "${MACHINE_ARCH}"

S = "${WORKDIR}"
B = "${S}/build"

#
# IMPORTANT:
#
# docker-rootfs recipe uses "docker" command (pre-installed in yocto build environment)
# to start a docker container to extract its complete filesystem
#
# IMPLICATION:
#
# 1. Using docker/dockerd-native for the hosttools in the bitbake architecture
#    is troublesome because docker requires a running dockerd in the hosttools,
#    and this is not easily setup within the bitbake build time. (Hence the
#    use of 'docker' command in the pre-installed yocto build environment)
#
# 2. By using pre-installed 'docker' command in yocto build environment, it
#    implies that we are using Host OS's dockerd, therefore the user(account)
#    bitbaking this docker-rootfs recipe needs access to dockerd's /var/run/docker.sock
#    on Host OS, thus allowing docker command to start a docker container.
#

inherit deploy
require docker-rootfs.inc

python () {
    # sets the yocto recipe's Package Version
    import re
    img = d.getVar("DOCKER_IMAGE", True)
    tag = d.getVar("DOCKER_TAG", True)
    pv = re.sub(r"[^a-z0-9A-Z_.-]", "_", "%s-%s" % (img,tag))
    d.setVar('PV', pv)
}

# default PV to docker tag
PV ?= "${DOCKER_TAG}"

# By default docker pull docker-hub's images
TARGET_DOCKER_IMAGE ?= "${DOCKER_IMAGE}:${DOCKER_TAG}"

do_patch[noexec] = "1"
do_configure[noexec] = "1"
do_package_qa[noexec] = "1"
do_populate_sysroot[noexec] = "1"

do_compile () {
  # Some sanity first for the docker variables
  if [ -z "${TARGET_DOCKER_IMAGE}" -o -z "${DOCKER_PLATFORM}" ]; then
    bbfatal "docker-rootfs: DOCKER_PLATFORM, DOCKER_IMAGE and/or DOCKER_TAG not set."
  fi

  # At this point we really need Internet connectivity for building the docker image
  if [ "x${@connected(d)}" != "xyes" ]; then
    bbfatal "docker-rootfs: Can't do do_compile as there is no Internet connectivity on this host."
  fi

  # We force the PATH to be the standard linux path in order to use the host's
  # docker daemon instead of the result of docker-native. This avoids version
  # mismatches
  DOCKER=$(PATH="${HOME}/bin:/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin" which docker)

  if DOCKER_CLI_EXPERIMENTAL=enabled ${DOCKER} manifest inspect ${DOCKER_PLATFORM}/${TARGET_DOCKER_IMAGE} >/dev/null; then
    bbnote "docker-rootfs: run docker image ${DOCKER_PLATFORM}/${TARGET_DOCKER_IMAGE} and wait for input..."
    RANDOM=$$
    CONTAINER_NAME="${DOCKER_IMAGE}-${DOCKER_TAG}-$RANDOM"
    ${DOCKER} run -d --name ${CONTAINER_NAME} --rm -t "${DOCKER_PLATFORM}/${TARGET_DOCKER_IMAGE}" bash -c read -n 1

    bbnote "docker-rootfs: export docker file system..."
    ${DOCKER} export -o ${B}/${DOCKER_IMAGE}-${DOCKER_TAG}.tar ${CONTAINER_NAME}

    bbnote "docker-rootfs: kill and clean up docker container..."
    ${DOCKER} container kill ${CONTAINER_NAME}
    ${DOCKER} container prune -f

    bbnote "docker-rootfs: clean up docker image..."
    ${DOCKER} rmi -f "${DOCKER_PLATFORM}/${TARGET_DOCKER_IMAGE}"
    ${DOCKER} image prune -f
  else
    bbfatal "docker-rootfs: No such image (${DOCKER_PLATFORM}/${TARGET_DOCKER_IMAGE}) on docker hub"
  fi
}

do_deploy () {
  install -d ${DEPLOY_DIR_IMAGE}
  if [ -f ${B}/${DOCKER_IMAGE}-${DOCKER_TAG}.tar ]; then
    install -m 644 ${B}/${DOCKER_IMAGE}-${DOCKER_TAG}.tar ${DEPLOY_DIR_IMAGE}/${DOCKER_IMAGE}-${DOCKER_TAG}.tar
  else
		bbfatal "${B}/${DOCKER_IMAGE}-${DOCKER_TAG}.tar not found. Please ensure docker-rootfs exported docker container file system correctly."
	fi
}
addtask deploy before do_package after do_install

do_install[fakeroot] = "1"

fakeroot do_install_append() {
	if [ -n "${D}" ]; then
		install -d ${D}
		if [ -f ${B}/${DOCKER_IMAGE}-${DOCKER_TAG}.tar ]; then
			tar xvf ${B}/${DOCKER_IMAGE}-${DOCKER_TAG}.tar -C ${D}
			rm -f ${D}/.dockerenv
		else
			bbfatal "${B}/${DOCKER_IMAGE}-${DOCKER_TAG}.tar not found. Please ensure docker-rootfs exported docker containers file system as tar file correctly."
		fi
	fi
}

FILES_${PN} = "/bin /etc /lib /mnt /proc /run /sys /usr /boot /dev /home /media /opt /root /sbin /tmp /var /srv"

