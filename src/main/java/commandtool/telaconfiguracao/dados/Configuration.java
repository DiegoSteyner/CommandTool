package commandtool.telaconfiguracao.dados;

import java.io.File;
import java.io.Serializable;

public class Configuration implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private File dirProgramas;
	private File mediaInfo;
	private File handBrake;
	private File mkvToolNix;

	public File getMediaInfo() {
		return mediaInfo;
	}
	
	public void setMediaInfo(File mediaInfo) {
		this.mediaInfo = mediaInfo;
	}
	
	public File getHandBrake() {
		return handBrake;
	}
	
	public void setHandBrake(File handBrake) {
		this.handBrake = handBrake;
	}
	
	public File getMkvToolNix() {
		return mkvToolNix;
	}
	
	public void setMkvToolNix(File mkvToolNix) {
		this.mkvToolNix = mkvToolNix;
	}
	
	public File getDirProgramas() {
		return dirProgramas;
	}
	
	public void setDirProgramas(File dirProgramas) {
		this.dirProgramas = dirProgramas;
	}
}
