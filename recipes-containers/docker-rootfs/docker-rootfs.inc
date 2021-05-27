DOCKERHUB_CHECK_CONN_URL ?= "index.docker.io"

# Check if host can reach a specific URL
# Used for connectivity check
def connected(d):
    import socket

    REMOTE_SERVER = d.getVar('DOCKERHUB_CHECK_CONN_URL', True)
    try:
        host = socket.gethostbyname(REMOTE_SERVER)
        testSocket = socket.create_connection((host, 80), 2)
        testSocket.close()
        return "yes"
    except:
        pass
    return "no"

# Check if docker is running and usable for current user
def usable_docker(d):
    import subprocess

    # Test docker execute permission
    cmd = "docker images > /dev/null 2>&1"
    child = subprocess.Popen(cmd, shell=True, stdout=subprocess.PIPE)
    cmd_output = child.communicate()[0]
    if child.returncode != 0:
        return "no"

    return "yes"

python () {
    # We need dockerd running on host
    if usable_docker(d) != 'yes':
        bb.fatal("docker-rootfs: Requires docker to run on your host OS and correct permission for current user.\n"
                 "(If building from container, you must be able to access host OS's docker.sock to create sibling containers.)")
}

# docker containers with specific CPU architectures
DOCKER_TAG ?= "latest"
DOCKER_IMAGE ?= "ubuntu"
DOCKER_PLATFORM ?= "amd64"
DOCKER_PLATFORM_aarch64 = "arm64v8"

