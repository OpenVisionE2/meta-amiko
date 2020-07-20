# We know about rtc
MACHINE_FEATURES_BACKFILL_CONSIDERED += "rtc"

MTD_KERNEL = "mtd2"
MTD_ROOTFS = "mtd0"
KERNEL_FILE = "kernel.bin"
ROOTFS_FILE = "rootfs.bin"

DVBPROVIDER = "kernel"
DVBSKYPROVIDER = "kernel"

GLIBC_EXTRA_OECONF = "--with-tls --with-__thread"

MACHINE_ESSENTIAL_EXTRA_RDEPENDS = "\
	amiko-dvb-modules-${MACHINE} \
	"

KERNEL_MODULE_AUTOLOAD += "xfs"

MACHINE_EXTRA_RRECOMMENDS += "\
	gstreamer1.0-plugin-dvbmediasink \
	"

DVBMEDIASINK_CONFIG = "--with-wma --with-wmv --with-pcm --with-dts --with-eac3 --with-h265 --with-vb6 --with-vb8 --with-spark"

TARGET_ARCH = "mipsel"
DEFAULTTUNE ?= "mips32el"

PREFERRED_PROVIDER_virtual/kernel = "linux-amiko"
PREFERRED_VERSION_linux-amiko = "4.10.6"
KERNEL_IMAGETYPE = "vmlinux"
KERNEL_OUTPUT = "${KERNEL_IMAGETYPE}"
KERNEL_CONSOLE = "ttyS0,115200n8"

IMAGE_FSTYPES ?= "ubi"

UBI_VOLNAME = "rootfs"

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
	rm -f ${DEPLOY_DIR_IMAGE}/*.ubi; \
	rm -f ${DEPLOY_DIR_IMAGE}/*.ubifs; \
	rm -Rf ${IMAGEDIR}; \
	"

IMAGE_CMD_ubi_prepend = " \
	rm -Rf ${IMAGE_ROOTFS}/tmp/*; \
	"

MACHINE_FEATURES += "alsa usbhost switchoff osdposition hdmicec textlcd videoenhancement blindscan-tbs HDMI"

# Broken bootloader can only flash around 100 MB
FLASHSIZE = "96"

require conf/machine/include/tune-mips32.inc

MKUBIFS_ARGS = "-m 2048 -e 126976 -c 4096"
UBINIZE_ARGS = "-m 2048 -p 128KiB"

CHIPSET = "bcm73625"

RCTYPE = "18"

BOX_BRAND = "amiko"
