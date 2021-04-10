SUMMARY = "player daemon helper"
MAINTAINER = "AMIKO"
PACKAGE_ARCH = "${MACHINE_ARCH}"

require conf/license/license-gplv2.inc

COMPATIBLE_MACHINE = "^(viper4k)$"

SRC_URI = "file://amiko-libreader-viper4k.sh"

INITSCRIPT_NAME = "amiko-libreader"
INITSCRIPT_PARAMS = "start 05 S ."

inherit update-rc.d

S = "${WORKDIR}"

do_compile() {
}

do_install() {
    install -d ${D}${INIT_D_DIR}/
    install -m 0755 ${WORKDIR}/amiko-libreader-viper4k.sh ${D}${INIT_D_DIR}/amiko-libreader
}
