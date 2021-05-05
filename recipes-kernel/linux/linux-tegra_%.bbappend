FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI_append_jetson-xavier-nx-devkit = " \
  file://tegra194-p3668-all-p3509-0000-nx-dlap301.dtsi \
  file://0001-dtb-include-adlink-additional-dtsi-from-tegra194-p36.patch \
  file://0001-BT-add-adlink-broadcom-bcm4356-VIP-PID-to-btusb-devi.patch \
"

do_patch_append_jetson-xavier-nx-devkit () {
  cp -f ${WORKDIR}/tegra194-p3668-all-p3509-0000-nx-dlap301.dtsi ${S}/nvidia/platform/t19x/jakku/kernel-dts/common/
}

