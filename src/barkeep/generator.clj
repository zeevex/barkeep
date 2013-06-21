(ns barkeep.generator
  (:require clojure.core.memoize))

(import com.google.zxing.client.j2se.MatrixToImageWriter
        com.google.zxing.common.BitMatrix
        com.google.zxing.BarcodeFormat
        com.google.zxing.MultiFormatWriter
        javax.imageio.ImageIO
        java.awt.image.BufferedImage)

;; 
;; See
;; http://zxing.org/w/docs/javadoc/com/google/zxing/BarcodeFormat.html
;;
;;
(defn translate-format [rawformat]
  (let [format (.replace rawformat \- \_)]
    (BarcodeFormat/valueOf   
     (.toUpperCase (case format
                     ("c128c" "c128" "c128a" "c128b") "CODE_128"
                     ("i2of5" "itf") "ITF"
                     ("c39") "CODE_39"
                     "ean13" "EAN_13"
                     "ean8"  "EAN_8"
                     ("qr" "qrcode") "QR_CODE"
                     "upca" "UPC_A"
                     "upce" "UPC_E"
                     format)))))

(defn make-barcode [value & {:keys [type height width]
                             :or   {type 'c128c height 200 width 400}}]
  (let [writer (new MultiFormatWriter)
        bctype (translate-format (.toString type))]
    (.encode writer value bctype width height)))

(defn to-image [barcode imageformat]
  (let [os (java.io.ByteArrayOutputStream.)]
    (ImageIO/write (MatrixToImageWriter/toBufferedImage barcode) imageformat os)
    (.toByteArray os)))

(defn make-barcode-image [value & {:keys [type height width imageformat]
                                   :or   {type 'c128c height 200 width 400
                                          imageformat "png"}}]
  (to-image
   (make-barcode value :type type :height height :width width)
   imageformat))

(def make-barcode-image-memoized (clojure.core.memoize/lru make-barcode-image))
