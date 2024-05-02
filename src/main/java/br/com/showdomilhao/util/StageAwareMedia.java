package br.com.showdomilhao.util;

import javafx.stage.Stage;

/**
 * Interface implementada pelos interessados que desejam ser notificados sobre o Stage
 * associado.
 */
public interface StageAwareMedia {

    /**
     * Indica que um Stage foi associado.
     *
     * @param stage Stage associado.
     */
    void onStageDefined(Stage stage);
}
