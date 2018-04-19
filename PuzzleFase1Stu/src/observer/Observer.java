package observer;

import java.io.File;
import java.util.List;

import org.jdom2.Element;

/**
 * Interfaz que representa un observador. Esta interfaz
 * serÃ¡ implementada por todos los modelos.
 * @author Miguel Ã�ngel
 * @version 1.0
 */
public interface Observer {

    /**
     * Actualiza el estado de los modelos observados
     * @param blankPos Posicion de la pieza vacÃ­a
     * @param movedPos Posicion de la pieza a mover
     */
    public void update(int blankPos,int movedPos);
    public void setNewBoard();
}
