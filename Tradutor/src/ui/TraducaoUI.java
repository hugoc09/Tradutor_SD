package ui;

import java.util.List;

import javax.swing.JOptionPane;

import network.TranslateListener;

public class TraducaoUI implements TranslateListener {

	
	@Override
	public void traducao(List<String> palavraTraduzida) {
		String palavra = palavraTraduzida.get(0);

		JOptionPane.showMessageDialog(null, palavra, "Tradu��o Portugu�s - Ingl�s",
				JOptionPane.INFORMATION_MESSAGE);
	}
	

}
