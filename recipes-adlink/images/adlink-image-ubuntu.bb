SUMMARY = "An empty image."
IMAGE_INSTALL = ""
IMAGE_LINGUAS = ""
PACKAGE_INSTALL = " docker-rootfs \
    kernel \
    u-boot-tegra-extlinux \
    tegra-firmware-xusb \
    ${ROOTFS_BOOTSTRAP_INSTALL} \
"
#    systemd-conf
#    tegra-minimal-init \
#    ${TEGRA_INITRD_BASEUTILS}
#    ${TEGRA_INITRD_INSTALL}

inherit image

# packagegroup-core-boot
#---> Package base-files.jetson_nano_devkit 3.0.14-r89 will be installed
#---> Package base-passwd.aarch64 3.5.29-r0 will be installed
#---> Package busybox.armv8a_tegra 1.31.1-r0 will be installed
#---> Package busybox-syslog.armv8a_tegra 1.31.1-r0 will be installed
#---> Package busybox-udhcpc.armv8a_tegra 1.31.1-r0 will be installed
#---> Package dbus-1.aarch64 1.12.16-r0 will be installed
#---> Package e2fsprogs-e2fsck.aarch64 1.45.4-r0 will be installed
#---> Package kbd.aarch64 2.2.0-r0 will be installed
#---> Package kbd-consolefonts.aarch64 2.2.0-r0 will be installed
#---> Package kbd-keymaps.aarch64 2.2.0-r0 will be installed

#---> Package kernel-image-4.9.201-l4t-r32.5+gc68230c5b46a.jetson_nano_devkit 4.9.201+git0+c68230c5b4-r0 will be installed
#---> Package kernel-image-image-4.9.201-l4t-r32.5+gc68230c5b46a.jetson_nano_devkit 4.9.201+git0+c68230c5b4-r0 will be installed
#---> Package kmod.aarch64 26-r0 will be installed

#---> Package ldconfig.aarch64 2.31+git0+f84949f1c4-r0 will be installed
#---> Package libacl1.aarch64 2.2.53-r0 will be installed
#---> Package libattr1.aarch64 2.4.48-r0 will be installed
#---> Package libblkid1.aarch64 2.35.1-r0 will be installed
#---> Package libc6.aarch64 2.31+git0+f84949f1c4-r0 will be installed
#---> Package libcap2.aarch64 2.32-r0 will be installed
#---> Package libcom-err2.aarch64 1.45.4-r0 will be installed
#---> Package libcrypt2.aarch64 4.4.15-r0 will be installed
#---> Package libdbus-1-3.aarch64 1.12.16-r0 will be installed
#---> Package libe2p2.aarch64 1.45.4-r0 will be installed
#---> Package libexpat1.aarch64 2.2.9-r0 will be installed
#---> Package libext2fs2.aarch64 1.45.4-r0 will be installed
#---> Package libkmod2.aarch64 26-r0 will be installed
#---> Package liblzma5.aarch64 5.2.4-r0 will be installed
#---> Package libmount1.aarch64 2.35.1-r0 will be installed
#---> Package libnss-myhostname2.aarch64 1:244.5-r0 will be installed
#---> Package libpam.aarch64 1.3.1-r0 will be installed
#---> Package libpam-runtime.aarch64 1.3.1-r0 will be installed
#---> Package libsystemd0.aarch64 1:244.5-r0 will be installed
#---> Package libuuid1.aarch64 2.35.1-r0 will be installed
#---> Package libx11-6.aarch64 1:1.6.9-r0 will be installed
#---> Package libxau6.aarch64 1:1.0.9-r0 will be installed
#---> Package libxcb1.aarch64 1.13.1-r0 will be installed
#---> Package libxdmcp6.aarch64 1:1.1.3-r0 will be installed
#---> Package libz1.aarch64 1.2.11-r0 will be installed
#---> Package netbase.aarch64 2:6.1-r0 will be installed
#---> Package os-release.noarch 1.0-r0 will be installed
#---> Package packagegroup-core-boot.jetson_nano_devkit 1.0-r17 will be installed
#---> Package pam-plugin-deny.aarch64 1.3.1-r0 will be installed
#---> Package pam-plugin-env.aarch64 1.3.1-r0 will be installed
#---> Package pam-plugin-faildelay.aarch64 1.3.1-r0 will be installed
#---> Package pam-plugin-group.aarch64 1.3.1-r0 will be installed
#---> Package pam-plugin-keyinit.aarch64 1.3.1-r0 will be installed
#---> Package pam-plugin-lastlog.aarch64 1.3.1-r0 will be installed
#---> Package pam-plugin-limits.aarch64 1.3.1-r0 will be installed
#---> Package pam-plugin-loginuid.aarch64 1.3.1-r0 will be installed
#---> Package pam-plugin-mail.aarch64 1.3.1-r0 will be installed
#---> Package pam-plugin-motd.aarch64 1.3.1-r0 will be installed
#---> Package pam-plugin-nologin.aarch64 1.3.1-r0 will be installed
#---> Package pam-plugin-permit.aarch64 1.3.1-r0 will be installed
#---> Package pam-plugin-rootok.aarch64 1.3.1-r0 will be installed
#---> Package pam-plugin-securetty.aarch64 1.3.1-r0 will be installed
#---> Package pam-plugin-shells.aarch64 1.3.1-r0 will be installed
#---> Package pam-plugin-unix.aarch64 1.3.1-r0 will be installed
#---> Package pam-plugin-warn.aarch64 1.3.1-r0 will be installed
#---> Package shadow.aarch64 4.8.1-r0 will be installed
#---> Package shadow-base.aarch64 4.8.1-r0 will be installed
#---> Package shadow-securetty.jetson_nano_devkit 4.6-r3 will be installed
#---> Package systemd.aarch64 1:244.5-r0 will be installed
#---> Package systemd-compat-units.aarch64 1.0-r29 will be installed
#---> Package systemd-conf.jetson_nano_devkit 244.3-r0 will be installed
#---> Package systemd-extra-utils.aarch64 1:244.5-r0 will be installed
#---> Package systemd-serialgetty.jetson_nano_devkit 1.0-r5 will be installed
#---> Package systemd-vconsole-setup.aarch64 1:244.5-r0 will be installed

#---> Package tegra-firmware.armv8a_tegra210 32.5.1-r0 will be installed
#---> Package tegra-firmware-container-csv.armv8a_tegra210 32.5.1-r0 will be installed
#---> Package tegra-firmware-tegra210.armv8a_tegra210 32.5.1-r0 will be installed
#---> Package tegra-firmware-tegra210-xusb.armv8a_tegra210 32.5.1-r0 will be installed
#---> Package tegra-firmware-xusb.armv8a_tegra210 32.5.1-r0 will be installed
#---> Package u-boot-tegra-extlinux.jetson_nano_devkit 1:2020.04+g0+f5e53cca90-r0 will be installed

#---> Package udev.aarch64 1:244.5-r0 will be installed
#---> Package udev-hwdb.aarch64 1:244.5-r0 will be installed
#---> Package update-alternatives-opkg.aarch64 0.4.2-r0 will be installed
#---> Package update-rc.d.noarch 0.8-r0 will be installed
#---> Package util-linux-agetty.aarch64 2.35.1-r0 will be installed
#---> Package util-linux-fsck.aarch64 2.35.1-r0 will be installed
#---> Package util-linux-mount.aarch64 2.35.1-r0 will be installed
#---> Package util-linux-sulogin.aarch64 2.35.1-r0 will be installed
#---> Package util-linux-umount.aarch64 2.35.1-r0 will be installed
#---> Package volatile-binds.noarch 1.0-r0 will be installed

