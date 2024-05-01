package br.com.showdomilhao.util;

import com.jfoenix.controls.JFXButton;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Uma extensão da classe JFXButton que exibe uma imagem ao lado do texto.
 */
public class ImageButton extends JFXButton {

    // Propriedade para armazenar a imagem a ser exibida
    private final ObjectProperty<Image> image = new SimpleObjectProperty<>();

    // Propriedades para armazenar a largura e altura da imagem
    private final DoubleProperty imageWidth = new SimpleDoubleProperty();
    private final DoubleProperty imageHeight = new SimpleDoubleProperty();

    /**
     * Construtor padrão que cria um botão sem texto.
     */
    public ImageButton() {
        super();
        // Definir o texto do botão como vazio
        setText("");
    }

    /**
     * Construtor que permite definir o texto e a imagem do botão.
     *
     * @param text  O texto a ser exibido no botão.
     * @param image A imagem a ser exibida no botão.
     */
    public ImageButton(String text, Image image) {
        super(text);
        setImage(image);
    }

    /**
     * Retorna a propriedade da imagem.
     *
     * @return A propriedade da imagem.
     */
    public final ObjectProperty<Image> imageProperty() {
        return this.image;
    }

    /**
     * Retorna a imagem atualmente definida para o botão.
     *
     * @return A imagem atualmente definida.
     */
    public final Image getImage() {
        return this.imageProperty().get();
    }

    /**
     * Define a imagem a ser exibida no botão.
     *
     * @param image A imagem a ser definida.
     */
    public final void setImage(final Image image) {
        this.imageProperty().set(image);
        updateGraphic();
    }

    /**
     * Retorna a propriedade da largura da imagem.
     *
     * @return A propriedade da largura da imagem.
     */
    public final DoubleProperty imageWidthProperty() {
        return this.imageWidth;
    }

    /**
     * Retorna a largura da imagem atualmente definida.
     *
     * @return A largura da imagem atualmente definida.
     */
    public final double getImageWidth() {
        return this.imageWidthProperty().get();
    }

    /**
     * Define a largura da imagem a ser exibida no botão.
     *
     * @param width A largura da imagem a ser definida.
     */
    public final void setImageWidth(final double width) {
        this.imageWidthProperty().set(width);
        updateGraphic();
    }

    /**
     * Retorna a propriedade da altura da imagem.
     *
     * @return A propriedade da altura da imagem.
     */
    public final DoubleProperty imageHeightProperty() {
        return this.imageHeight;
    }

    /**
     * Retorna a altura da imagem atualmente definida.
     *
     * @return A altura da imagem atualmente definida.
     */
    public final double getImageHeight() {
        return this.imageHeightProperty().get();
    }

    /**
     * Define a altura da imagem a ser exibida no botão.
     *
     * @param height A altura da imagem a ser definida.
     */
    public final void setImageHeight(final double height) {
        this.imageHeightProperty().set(height);
        updateGraphic();
    }

    /**
     * Atualiza o gráfico do botão com a imagem e suas dimensões definidas.
     */
    private void updateGraphic() {
        Image img = getImage();
        if (img != null) {
            ImageView imageView = new ImageView(img);
            double width = getImageWidth();
            double height = getImageHeight();
            // Definir a largura e altura do ImageView
            imageView.setFitWidth(width);
            imageView.setFitHeight(height);
            setGraphic(imageView);
        } else {
            // Se a imagem for nula, usar o gráfico padrão do botão (apenas texto)
            setGraphic(getGraphic());
        }
    }
}
