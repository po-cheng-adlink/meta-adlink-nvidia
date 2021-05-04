# Copyright (C) 2021 ADLINK Semiconductor
SUMMARY = "ADLINK DIO Kernel Module"
LICENSE = "GPL2.0"
LIC_FILES_CHKSUM = "file://GPL-2.0;md5=801f80980d171dd6425610833a22dbe6"

inherit module

SRC_URI = "git://github.com/po-cheng-adlink/adlink-dio.git;branch=adlink-dio;protocol=https;"
SRCREV = "ba2ae1d6b455a7d01b77ff37e61e3c1b8be6e0a9"

S = "${WORKDIR}/git"

