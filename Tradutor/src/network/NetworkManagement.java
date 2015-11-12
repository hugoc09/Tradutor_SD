package network;

import java.util.List;

import org.json.JSONException;

import ui.TraducaoUI;
import util.Traducao;

public class NetworkManagement {

	public static void traduzir(String palavra) throws JSONException {

		String json = Traducao.getData(palavra);

		List<String> palavrasTraduzidas = Traducao.parseJson(json);

		TranslateListener tl = new TraducaoUI();

		Dispatcher.getInstance().registrarEvento(tl);
		
		

		Dispatcher.getInstance().dispatchEvent(palavrasTraduzidas);
	}

}
