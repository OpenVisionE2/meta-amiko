# We know about rtc
MACHINE_FEATURES_BACKFILL_CONSIDERED += "rtc"

KERNELVERSION = "4.10.6"

MTD_KERNEL = "mtd2"
MTD_ROOTFS = "mtd0"
KERNEL_FILE = "kernel.bin"
ROOTFS_FILE = "rootfs.bin"

DVBPROVIDER = "kernel"
DVBSKYPROVIDER = "kernel"

GLIBC_EXTRA_OECONF = "--with-tls --with-__thread"

MACHINE_ESSENTIAL_EXTRA_RDEPENDS += "\
	amiko-dvb-modules-${MACHINE} \
	gstreamer1.0-plugin-dvbmediasink \
	"

DVBMEDIASINK_CONFIG = "--with-wma --with-wmv --with-pcm --with-dts --with-eac3 --with-h265 --with-vb6 --with-vb8 --with-spark"

TARGET_ARCH = "mipsel"
DEFAULTTUNE = "mips32el"

PREFERRED_PROVIDER_virtual/kernel = "linux-amiko"
PREFERRED_VERSION_linux-amiko = "${KERNELVERSION}"
KERNEL_IMAGETYPE = "vmlinux"
KERNEL_OUTPUT = "${KERNEL_IMAGETYPE}"
KERNEL_CONSOLE = "ttyS0,115200n8"

IMAGE_FSTYPES ?= "ubi"

IMAGE_CMD_ubi_append = " \
	mkdir -p ${IMGDEPLOYDIR}/${IMAGEDIR}; \
	cp -f ${IMGDEPLOYDIR}/${IMAGE_NAME}${IMAGE_NAME_SUFFIX}.ubi ${IMGDEPLOYDIR}/${IMAGEDIR}/${ROOTFS_FILE}; \
	gzip -9c ${DEPLOY_DIR_IMAGE}/vmlinux-${MACHINE}.bin > ${IMGDEPLOYDIR}/${IMAGEDIR}/${KERNEL_FILE}; \
	echo "${IMAGE_NAME}" > ${IMGDEPLOYDIR}/${IMAGEDIR}/imageversion; \
	echo "https://forum.openvision.tech/app.php/donate" > ${IMGDEPLOYDIR}/${IMAGEDIR}/donate.txt; \
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

IMAGE_CMD_ubifs_prepend = " \
	rm -Rf ${IMAGE_ROOTFS}/boot/*; \
	rm -Rf ${IMAGE_ROOTFS}/tmp/*; \
	"

MACHINE_FEATURES += "alsa usbhost switchoff osdposition hdmicec textlcd videoenhancement blindscan-tbs HDMI"

# Broken bootloader can only flash around 100 MB
FLASHSIZE = "96"

require conf/machine/include/tune-mips32.inc

MKUBIFS_ARGS = "-m 2048 -e 126976 -c 4096"
UBINIZE_ARGS = "-m 2048 -p 128KiB"

SOC_FAMILY = "bcm73625"

RCTYPE = "18"

BOX_BRAND = "amiko"

RCNAME ?= "amiko2"
RCIDNUM ?= "1"
