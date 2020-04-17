SUMMARY = "${MACHINE} partitions files"
SECTION = "base"
PRIORITY = "required"
LICENSE = "CLOSED"
require conf/license/license-close.inc
PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit deploy

SRCDATE = "20200417"

COMPATIBLE_MACHINE = "^(viper4k)$"

S = "${WORKDIR}/patitions"

SRC_URI = "http://source.mynonpublic.com/amiko/${MACHINE}-partitions-${SRCDATE}.zip"

SRC_URI[md5sum] = "2703b29d6fc68a840bc53f520ebd1656"
SRC_URI[sha256sum] = "ae3b028e9b38113dc0045de268b64e9d2b4ecd57b8f05df66f460826e5e4eb98"

ALLOW_EMPTY_${PN} = "1"
do_configure[nostamp] = "1"

do_install() {
    install -d ${D}${datadir}
    install -m 0644 ${S}/bootargs.bin ${D}${datadir}/bootargs.bin
    install -m 0644 ${S}/fastboot.bin ${D}${datadir}/fastboot.bin
    install -m 0644 ${S}/apploader.bin ${D}${datadir}/apploader.bin
}

FILES_${PN} = "${datadir}"

do_deploy() {
    install -d ${DEPLOY_DIR_IMAGE}/${MACHINE}-partitions
    install -m 0755 ${S}/bootargs.bin ${DEPLOY_DIR_IMAGE}/${MACHINE}-partitions
    install -m 0755 ${S}/boot.img ${DEPLOY_DIR_IMAGE}/${MACHINE}-partitions
    install -m 0755 ${S}/fastboot.bin ${DEPLOY_DIR_IMAGE}/${MACHINE}-partitions
    install -m 0755 ${S}/apploader.bin ${DEPLOY_DIR_IMAGE}/${MACHINE}-partitions
    install -m 0755 ${S}/pq_param.bin ${DEPLOY_DIR_IMAGE}/${MACHINE}-partitions
    install -m 0755 ${S}/emmc_partitions.xml ${DEPLOY_DIR_IMAGE}/${MACHINE}-partitions
    install -m 0755 ${S}/baseparam.img ${DEPLOY_DIR_IMAGE}/${MACHINE}-partitions
    install -m 0755 ${S}/logo.img ${DEPLOY_DIR_IMAGE}/${MACHINE}-partitions
    install -m 0755 ${S}/deviceinfo.bin ${DEPLOY_DIR_IMAGE}/${MACHINE}-partitions
}

addtask deploy before do_build after do_install

