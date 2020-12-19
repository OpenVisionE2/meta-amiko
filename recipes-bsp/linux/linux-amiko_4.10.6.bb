DESCRIPTION = "Linux kernel for ${MACHINE}"
SECTION = "kernel"
LICENSE = "GPLv2"

PACKAGE_ARCH = "${MACHINE_ARCH}"

KERNEL_RELEASE = "${KERNELVERSION}"
SRCDATE = "20170831"

inherit kernel machine_kernel_pr samba_change_dialect

COMPATIBLE_MACHINE = "^(vipert2c|vipercombo|vipercombohdd|viperslim)$"

SRC_URI[md5sum] = "cb28a0056279ad46af776fd5cdbcf88f"
SRC_URI[sha256sum] = "a147b639445206ce85a56e155667ada38db1512e1c78fe9d4fd6d4be6f5a2e0e"

LIC_FILES_CHKSUM = "file://${WORKDIR}/linux-${PV}-base/COPYING;md5=d7810fab7487fb0aad327b76f1be7cd7"

# By default, kernel.bbclass modifies package names to allow multiple kernels
# to be installed in parallel. We revert this change and rprovide the versioned
# package names instead, to allow only one kernel to be installed.
PKG_${KERNEL_PACKAGE_NAME}-base = "${KERNEL_PACKAGE_NAME}-base"
PKG_${KERNEL_PACKAGE_NAME}-image = "${KERNEL_PACKAGE_NAME}-image"
RPROVIDES_${KERNEL_PACKAGE_NAME}-base = "${KERNEL_PACKAGE_NAME}-${KERNEL_VERSION}"
RPROVIDES_${KERNEL_PACKAGE_NAME}-image = "${KERNEL_PACKAGE_NAME}-image-${KERNEL_VERSION}"

SRC_URI = "http://source.mynonpublic.com/entwopia/yh625tc/yh625tc-linux-${PV}-base-${SRCDATE}.tgz \
	file://defconfig \
	file://${OPENVISION_BASE}/meta-openvision/recipes-linux/kernel-patches/kernel-add-support-for-gcc${VISIONGCCVERSION}.patch \
	file://0001-add-dmx-source-timecode.patch \
	file://0002-nand-ecc-strength-and-bitflip.patch \
	file://TBS-fixes-for-4.10-kernel.patch \
	file://0001-Support-TBS-USB-drivers-for-4.6-kernel.patch \
	file://0001-TBS-fixes-for-4.6-kernel.patch \
	file://0001-STV-Add-PLS-support.patch \
	file://0001-STV-Add-SNR-Signal-report-parameters.patch \
	file://blindscan2.patch \
	file://0001-stv090x-optimized-TS-sync-control.patch \
	file://0001-revert-xhci-plat.patch \
	file://noforce_correct_pointer_usage.patch \
	file://0003-cp1emu-do-not-use-bools-for-arithmetic.patch \
	file://0003-makefile-disable-warnings.patch \
	"

S = "${WORKDIR}/linux-${PV}-base"

export OS = "Linux"
KERNEL_OBJECT_SUFFIX = "ko"
KERNEL_OUTPUT = "vmlinux"
KERNEL_OUTPUT_DIR = "."
KERNEL_IMAGETYPE = "vmlinux"
KERNEL_IMAGEDEST = "tmp"

FILES_${KERNEL_PACKAGE_NAME}-image = "/${KERNEL_IMAGEDEST}/${KERNEL_IMAGETYPE}*"

kernel_do_install_append() {
	${STRIP} ${D}/${KERNEL_IMAGEDEST}/${KERNEL_IMAGETYPE}-${KERNEL_VERSION}
	gzip -9c ${D}/${KERNEL_IMAGEDEST}/${KERNEL_IMAGETYPE}-${KERNEL_VERSION} > ${D}/${KERNEL_IMAGEDEST}/${KERNEL_IMAGETYPE}.gz
	rm -f ${D}/${KERNEL_IMAGEDEST}/${KERNEL_IMAGETYPE}-${KERNEL_VERSION}
}

pkg_postinst_${KERNEL_PACKAGE_NAME}-image () {
	if [ "x$D" == "x" ]; then
		if [ -f /${KERNEL_IMAGEDEST}/${KERNEL_IMAGETYPE}.gz ] ; then
			flash_eraseall /dev/${MTD_KERNEL}
			nandwrite -p /dev/${MTD_KERNEL} /${KERNEL_IMAGEDEST}/${KERNEL_IMAGETYPE}.gz
			rm -f /${KERNEL_IMAGEDEST}/${KERNEL_IMAGETYPE}.gz
		fi
	fi
	true
}

do_rm_work() {
}
