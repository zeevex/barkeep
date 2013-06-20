(ns barkeep.generator)

(import com.google.zxing.client.j2se.MatrixToImageWriter
        com.google.zxing.common.BitMatrix
        com.google.zxing.BarcodeFormat
        com.google.zxing.MultiFormatWriter
        javax.imageio.ImageIO
        java.awt.image.BufferedImage)

(defn translate-format [format]
  (BarcodeFormat/valueOf
   (case format
     ("c128c" "c128") "CODE_128"
     format)))

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

;; com.google.zxing.client.j2se.MatrixToImageWriter.writeToFile(matrix,"PNG",new File("some/path.png"));
;; com.google.zxing.client.j2se.MatrixToImageWriter.writeToStream(matrix, "PNG", outputStream);
;; java.awt.BufferedImage image =
;;    com.google.zxing.client.j2se.MatrixToImageWriter.toBufferedImage(matrix)
;;-----
;; bitMatrix = new Code128Writer().encode("Hello World !!!",BarcodeFormat.CODE_128,width,height,null);
;; MatrixToImageWriter.writeToStream(bitMatrix, "png", new FileOutputStream(new File("/home/kas/zxing_barcode.png")));
