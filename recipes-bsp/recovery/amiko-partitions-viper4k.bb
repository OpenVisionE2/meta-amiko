SUMMARY = "${MACHINE} partitions files"
SECTION = "base"
PRIORITY = "required"
LICENSE = "CLOSED"
require conf/license/license-close.inc
PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit deploy update-rc.d

SRCDATE = "20201218"

PV = "${SRCDATE}"

COMPATIBLE_MACHINE = "^(viper4k)$"

S = "${WORKDIR}/patitions"

SRC_URI = "http://source.mynonpublic.com/amiko/${MACHINE}-partitions-${SRCDATE}.zip \
  file://flash-apploader \
"

INITSCRIPT_NAME = "flash-apploader"
INITSCRIPT_PARAMS = "start 90 S ."

SRC_URI[md5sum] = "89eca0156e7ffecc290a75967c6f86ad"
SRC_URI[sha256sum] = "a9ccb848e9a0aebc61c6cd74fd19de6bf5e2d07f09f762b7bcde9d87e178a332"

ALLOW_EMPTY_${PN} = "1"
do_configure[nostamp] = "1"

do_install() {
    install -d ${D}${datadir}
    install -m 0644 ${S}/bootargs.bin ${D}${datadir}/bootargs.bin
    install -m 0644 ${S}/fastboot.bin ${D}${datadir}/fastboot.bin
    install -m 0644 ${S}/apploader.bin ${D}${datadir}/apploader.bin
    install -m 0755 -d ${D}${INIT_D_DIR}
    install -m 0755 ${WORKDIR}/flash-apploader ${D}${INIT_D_DIR}/flash-apploader
}

FILES_${PN} = "${datadir} ${sysconfdir}"

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
