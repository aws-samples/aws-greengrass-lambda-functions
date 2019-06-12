package com.amazonaws.greengrass.cddsensehat.leds.arrays;

/*
public class MacOSJavaFXLEDArray extends Application implements JavaFXLEDArray {
    public static final int PIXEL_SIZE = 80;
    private static Canvas canvas;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("LED array");
        Group root = new Group();
        Canvas tempCanvas = new Canvas(640, 640);
        root.getChildren().add(tempCanvas);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

        canvas = tempCanvas;
    }

    @Override
    public synchronized void draw(SenseHatLEDImage senseHatLEDImage) {
        if (canvas == null) {
            new Thread(() -> MacOSJavaFXLEDArray.launch()).start();

            waitForInitialization();

            if (canvas == null) {
                // This can happen if we exit the waitForInitialization method via an exception
                return;
            }
        }

        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();

        int position = 0;

        for (SenseHatLED senseHatLED : senseHatLEDImage.renderToLEDs()) {
            int x = (position % 8) * PIXEL_SIZE;
            int y = (position / 8) * PIXEL_SIZE;

            Color color = new Color(senseHatLED.getRed() / 255.0, senseHatLED.getGreen() / 255.0, senseHatLED.getBlue() / 255.0, 1.0);

            graphicsContext.setFill(color);
            graphicsContext.fillRect(x, y, PIXEL_SIZE, PIXEL_SIZE);

            position++;
        }
    }

    private void waitForInitialization() {
        while (canvas == null) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
*/
