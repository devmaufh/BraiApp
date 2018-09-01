package maufdh.dev.brailyapp.Watson;

import com.ibm.watson.developer_cloud.speech_to_text.v1.SpeechToText;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.RecognizeOptions;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.SpeechRecognitionResults;
import com.ibm.watson.developer_cloud.speech_to_text.v1.websocket.BaseRecognizeCallback;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;

public class WatsonSpeechToText {
    public static String getText(String path){
        final String[] finalText = new String[1];
        SpeechToText speechToText= new SpeechToText();
        speechToText.setUsernameAndPassword("36196d71-5dd8-4c92-bcac-3d0ac65e5e03","YDk2gbXni6R6");
        try{
            RecognizeOptions recognizeOptions = new RecognizeOptions.Builder()
                    .audio(new File(path))
                    .contentType("audio/mp3")
                    .model("en-US_BroadbandModel")
                    .build();

            BaseRecognizeCallback baseRecognizeCallback= new BaseRecognizeCallback(){
                @Override
                public void onTranscription(SpeechRecognitionResults speechResults) {
                    super.onTranscription(speechResults);
                    finalText[0] =speechResults.toString();
                }

                @Override
                public void onDisconnected() {
                    super.onDisconnected();
                    finalText[0]="Error al reconocer voz.";

                }
            };
            speechToText.recognizeUsingWebSocket(recognizeOptions,baseRecognizeCallback);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return finalText[0];
    }
}
