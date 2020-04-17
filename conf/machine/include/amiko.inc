# We know about rtc
MACHINE_FEATURES_BACKFILL_CONSIDERED += "rtc"

MACHINE_ESSENTIAL_EXTRA_RDEPENDS = "\
	amiko-dvb-modules-${MACHINE} \
	"

MACHINE_ESSENTIAL_EXTRA_RRECOMMENDS = "\
	kernel-module-cdfs \
	"

KERNEL_MODULE_AUTOLOAD += "xfs"

MACHINE_EXTRA_RRECOMMENDS = " \
	${@bb.utils.contains("GST_VERSION", "1.0", "gstreamer1.0-plugin-dvbmediasink", "gst-plugin-dvbmediasink", d)} \
	ntfs-3g \
	"

DVBMEDIASINK_CONFIG = "--with-wma --with-wmv --with-pcm --with-dts --with-eac3 --with-h265 --with-vb6 --with-vb8 --with-spark"

TARGET_ARCH = "mipsel"
DEFAULTTUNE ?= "mips32el"

PREFERRED_PROVIDER_virtual/kernel = "linux-amiko"
PREFERRED_VERSION_linux-amiko = "4.10.6"
KERNEL_IMAGETYPE = "vmlinux"
KERNEL_OUTPUT = "${KERNEL_IMAGETYPE}"
KERNEL_CONSOLE = "null"

IMAGE_FSTYPES ?= "ubi"

UBI_VOLNAME = "rootfs"

IMAGEDIR ?= "${MACHINE}"

FORCE ?= "no"

IMGDEPLOYDIR ?= "${DEPLOY_DIR_IMAGE}"

IMAGE_CMD_ubi_append = " \
	mkdir -p ${IMGDEPLOYDIR}/${IMAGEDIR}; \
	cp ${IMGDEPLOYDIR}/${IMAGE_NAME}${IMAGE_NAME_SUFFIX}.ubi ${IMGDEPLOYDIR}/${IMAGEDIR}/rootfs.bin; \
	gzip -9c ${DEPLOY_DIR_IMAGE}/vmlinux-${MACHINE}.bin > ${IMGDEPLOYDIR}/${IMAGEDIR}/kernel.bin; \
	echo "${IMAGE_NAME}" > ${IMGDEPLOYDIR}/${IMAGEDIR}/imageversion; \
	if [ "${FORCE}" = "yes" ]; then \
		echo "rename this file to 'noforce' to force an update with confirmation" > ${IMGDEPLOYDIR}/${IMAGEDIR}/force; \
	else \
		echo "rename this file to 'force' to force an update without confirmation" >${IMGDEPLOYDIR}/${IMAGEDIR}/noforce; \
	fi; \
	rm -f ${IMGDEPLOYDIR}/*.zip; \
	cd ${IMGDEPLOYDIR}; \
	zip ${IMAGE_NAME}_usb.zip ${IMAGEDIR}/*; \
	rm -Rf ${IMAGEDIR}; \
	"

IMAGE_CMD_ubi_prepend = " \
	rm -Rf ${IMAGE_ROOTFS}/tmp/*; \
	"

MACHINE_FEATURES += "alsa usbhost wlan 3dtv switchoff osdposition hdmicec textlcd 7seg"

require conf/machine/include/tune-mips32.inc
require conf/machine/include/amiko-wifi.inc

MKUBIFS_ARGS = "-m 2048 -e 126976 -c 4096"
UBINIZE_ARGS = "-m 2048 -p 128KiB -s 2048"

CHIPSET = "bcm73625"

RCTYPE = "18"

BOX_BRAND = "amiko"
