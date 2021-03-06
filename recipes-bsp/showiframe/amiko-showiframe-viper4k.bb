SUMMARY = "showiframe for ${MACHINE}"
SECTION = "base"
PRIORITY = "optional"
LICENSE = "CLOSED"
PACKAGE_ARCH = "${MACHINE_ARCH}"

COMPATIBLE_MACHINE = "^(viper4k)$"

SRCDATE = "20180301"

PV = "${SRCDATE}"

RPROVIDES_${PN}  = "showiframe"
RREPLACES_${PN}  = "showiframe"
RCONFLICTS_${PN} = "showiframe"

SRC_URI = "http://source.mynonpublic.com/amiko/${MACHINE}-showiframe-${SRCDATE}.tar.gz"

SRC_URI[md5sum] = "898ccb22efbcc90123fdc9c1d794b078"
SRC_URI[sha256sum] = "8c691126d8d505c3943ebbcf55a50adfef6a38787d2d255863c4a43362f527a3"

S = "${WORKDIR}"

do_install() {
    install -d ${D}${bindir}
    install -m 0755 ${S}/showiframe ${D}${bindir}
}

do_package_qa() {
}

FILES_${PN}  = "${bindir}/showiframe"
