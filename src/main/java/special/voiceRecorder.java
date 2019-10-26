package special;

import net.dv8tion.jda.api.audio.AudioReceiveHandler;
import net.dv8tion.jda.api.audio.CombinedAudio;
import net.dv8tion.jda.api.audio.UserAudio;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class voiceRecorder implements AudioReceiveHandler {

    private List<byte[]> rescievedBytes = new ArrayList<>();

    @Override
    public boolean canReceiveCombined() {
        return true;
    }

    @Override
    public boolean canReceiveUser() {
        return true;
    }

    @Override
    public void handleCombinedAudio(CombinedAudio combinedAudio) {
        File out = new File("out10.wav");
        try {
            rescievedBytes.add(combinedAudio.getAudioData(100));
        } catch (OutOfMemoryError e) {
            //close connection
        }
        try {
            int size = 0;
            for (byte[] bs : rescievedBytes) {
                size += bs.length;
            }
            byte[] decodedData = new byte[size];
            int i = 0;
            for (byte[] bs : rescievedBytes) {
                for (int j = 0; j < bs.length; j++) {
                    decodedData[i] = bs[j];
                }
            }
            getWavFile(out, decodedData);
        } catch (IOException | OutOfMemoryError e) {
            e.printStackTrace();
        }
    }

    private void getWavFile(File outFile, byte[] decodedData) throws IOException {
        AudioFormat format = new AudioFormat(8000, 16, 1, true, false);
        AudioSystem.write(new AudioInputStream(new ByteArrayInputStream(
                decodedData), format, decodedData.length), AudioFileFormat.Type.WAVE, outFile);
    }

    @Override
    public void handleUserAudio(UserAudio userAudio) {

    }
}
