DESCRIPTION = "Linux kernel for ${MACHINE}"
SECTION = "kernel"
LICENSE = "GPLv2"
PACKAGE_ARCH = "${MACHINE_ARCH}"

KERNEL_RELEASE = "4.4.35"
SRCDATE = "20181224"

inherit kernel machine_kernel_pr samba_change_dialect

COMPATIBLE_MACHINE = "^(viper4k)$"

SRC_URI[md5sum] = "ad7eab17a5071a0d5f9ff44eb44e027d"
SRC_URI[sha256sum] = "0654d5aa21c51eaea46f7203014afe60052ec0990a92b9e289e1ca8a2793907c"

LIC_FILES_CHKSUM = "file://${WORKDIR}/linux-${PV}/COPYING;md5=d7810fab7487fb0aad327b76f1be7cd7"

# By default, kernel.bbclass modifies package names to allow multiple kernels
# to be installed in parallel. We revert this change and rprovide the versioned
# package names instead, to allow only one kernel to be installed.
PKG_${KERNEL_PACKAGE_NAME}-base = "${KERNEL_PACKAGE_NAME}-base"
PKG_${KERNEL_PACKAGE_NAME}-image = "${KERNEL_PACKAGE_NAME}-image"
RPROVIDES_${KERNEL_PACKAGE_NAME}-base = "${KERNEL_PACKAGE_NAME}-${KERNEL_VERSION}"
RPROVIDES_${KERNEL_PACKAGE_NAME}-image = "${KERNEL_PACKAGE_NAME}-image-${KERNEL_VERSION}"

SRC_URI += "http://source.mynonpublic.com/amiko/amiko-linux-${PV}-${SRCDATE}.tar.gz \
    file://defconfig \
    file://${OPENVISION_BASE}/meta-openvision/recipes-linux/kernel-patches/kernel-add-support-for-gcc${VISIONGCCVERSION}.patch \
    file://0001-remote.patch \
    file://HauppaugeWinTV-dualHD.patch \
    file://dib7000-linux_4.4.179.patch \
    file://dvb-usb-linux_4.4.179.patch \
    file://initramfs-subdirboot.cpio.gz;unpack=0 \
    file://findkerneldevice.sh \
    file://0002-log2-give-up-on-gcc-constant-optimizations.patch \
    file://0003-dont-mark-register-as-const.patch \
    file://wifi-linux_4.4.183.patch \
    file://fix-dvbcore.patch \
    file://0005-xbox-one-tuner-4.4.patch \
    file://0006-dvb-media-tda18250-support-for-new-silicon-tuner.patch \
    file://0007-dvb-mn88472-staging.patch \
    file://mn88472_reset_stream_ID_reg_if_no_PLP_given.patch \
"

S = "${WORKDIR}/linux-${PV}"
B = "${WORKDIR}/build"

export OS = "Linux"
KERNEL_OBJECT_SUFFIX = "ko"
KERNEL_IMAGEDEST = "tmp"

FILES_${KERNEL_PACKAGE_NAME}-image = "/${KERNEL_IMAGEDEST}/${KERNEL_IMAGETYPE} /${KERNEL_IMAGEDEST}/findkerneldevice.sh"

KERNEL_IMAGETYPE = "uImage"
KERNEL_OUTPUT = "arch/${ARCH}/boot/${KERNEL_IMAGETYPE}"

kernel_do_configure_prepend() {
	install -d ${B}/usr
	install -m 0644 ${WORKDIR}/initramfs-subdirboot.cpio.gz ${B}/
}

kernel_do_install_append() {
	install -d ${D}/${KERNEL_IMAGEDEST}
	install -m 0755 ${KERNEL_OUTPUT} ${D}/${KERNEL_IMAGEDEST}
	install -m 0755 ${WORKDIR}/findkerneldevice.sh ${D}/${KERNEL_IMAGEDEST}
}

pkg_postinst_kernel-image () {
    if [ "x$D" == "x" ]; then
        if [ -f /${KERNEL_IMAGEDEST}/${KERNEL_IMAGETYPE} ] ; then
            /${KERNEL_IMAGEDEST}/./findkerneldevice.sh
            dd if=/${KERNEL_IMAGEDEST}/${KERNEL_IMAGETYPE} of=/dev/kernel
        fi
    fi
    true
}

do_rm_work() {
}
