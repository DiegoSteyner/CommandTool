package commandtool.mkvtoolnix;

import java.util.ArrayList;

import jutil.data.dtos.PrintOutputDTO;
import jutil.utils.RuntimeUtils;

public class MkvToolnix 
{
	private String id = "[id]";
	private String languageId = "[languageId]";
	private String trackName = "[nomeTrilha]";
	
	private String language = " --language "+id+":"+languageId+" ";
	private String nomeTrilha = " --track-name \""+id+":"+trackName+"\" ";
	private String trilhaPadrao = " --default-track "+id+":yes ";
	private String audioTrack = " --audio-tracks "+id+" ";
	private String subtitleTrack = " --subtitle-tracks "+id+" ";
	private String forcarTrilha = " --forced-track "+id+":yes ";

	private String noSubtitles = " --no-subtitles "; 
	private String noAttachaments = " --no-attachments "; 
	private String noChapters = " --no-chapters "; 
	
	private String enderecoMkvToolNix = "";
	private StringBuilder command = new StringBuilder();
	
	private ArrayList<MkvTrack> trilhasExtras = new ArrayList<MkvTrack>();
	
	public MkvToolnix(String enderecoMkvToolNix)
	{
		this.enderecoMkvToolNix = " \""+enderecoMkvToolNix+"\" ";
		command.append(this.enderecoMkvToolNix);
	}
	
	public MkvToolnix selectAudioTrack(int id)
	{
		command.append(audioTrack.replace(this.id, String.valueOf(id)));
		return(this);
	}

	public MkvToolnix selectSubtitleTrack(int id)
	{
		command.append(subtitleTrack.replace(this.id, String.valueOf(id)));
		return(this);
	}
	
	public MkvToolnix setNoSubtitles()
	{
		command.append(noSubtitles);
		return(this);
	}
	
	public MkvToolnix setNoAttachaments()
	{
		command.append(noAttachaments);
		return(this);
	}

	public MkvToolnix setNoChapters()
	{
		command.append(noChapters);
		return(this);
	}
	
	public MkvToolnix configureTrack(MkvTrack track)
	{
		return configureTrack(track, false);
	}
	
	private MkvToolnix configureTrack(MkvTrack track, boolean addFile)
	{
		if(track.getLinguagemId() != null && !track.getLinguagemId().isEmpty())
		{
			command.append(language.replace(id, track.getId()).replace(languageId, track.getLinguagemId()));
		}
		
		command.append(this.nomeTrilha.replace(id, track.getId()).replace(trackName, track.getNome()));
		
		if(track.isPadrao())
		{
			command.append(this.trilhaPadrao.replace(id, track.getId()));
		}
		
		if(track.isForcada())
		{
			command.append(this.forcarTrilha.replace(id, track.getId()));
		}
		
		if(addFile)
		{
			command.append("\"(\" \""+track.getFile().getAbsolutePath()+"\" \")\" ");
		}
		
		return(this);
	}
	
	public MkvToolnix addTrilhaExterna(MkvTrack track) throws Exception
	{
		if(track.getFile() != null && track.getFile().exists())
		{
			trilhasExtras.add(track);
			return(this);
		}
		
		throw new Exception("O arquivo da trilha externa ["+track.getFile()+"] não existe ou está nulo.");
	}
	
	public String buildCommand(String inputFile, String outputFile)
	{
		command.append(" --output \"").append(outputFile).append("\" \"").append(inputFile).append("\"");
		
		for(MkvTrack track : trilhasExtras)
		{
			configureTrack(track, true);
		}
		
		String str = command.toString();
		command = new StringBuilder(enderecoMkvToolNix);
		trilhasExtras.clear();
		
		return(str);
	}
	
	public static void main(String[] args) {
		
		MkvToolnix mkv = new MkvToolnix("G:/Programas Temp/MkvToolnix x64 v37.0.0\\mkvmerge.exe");
		
		try 
		{
			mkv.selectAudioTrack(1)
			.configureTrack(new MkvTrack(0, "A vida secreta dos Pets", "por", true, true))
			.setNoAttachaments()
			.setNoChapters()
			.addTrilhaExterna(new MkvTrack(0, "Legenda externa forçada", "por", true, true, new java.io.File("E:\\filmes\\filme - pets - a vida secreta dos bixos 2\\Pets.A.Vida.Secreta.dos.Bichos.2.2019.1080p.BluRay.x264-iFT.DUAL-RK\\Pets.A.Vida.Secreta.dos.Bichos.2.2019.1080p.BluRay.x264-iFT.DUAL-RK_Forced.srt")))
			.configureTrack(new MkvTrack(1, "Português Brasil", "por", true, true))
			.setNoSubtitles()
			;
			//System.out.println(mkv.buildCommand("E:\\filme - pets - a vida secreta dos bixos 2\\Pets.A.Vida.Secreta.dos.Bichos.2.2019.1080p.BluRay.x264-iFT.DUAL-RK\\Pets.A.Vida.Secreta.dos.Bichos.2.2019.1080p.BluRay.x264-iFT.DUAL-RK.mkv", "G:\\Prontos\\Saida de vídeo command.mkv"));
		
//			String command = "\"G:\\CLI\\MkvToolnix x64 v37.0.0\\mkvmerge.exe\"  --no-attachments  --no-chapters  --audio-tracks 1  --language 0:pt  --track-name \"0:Pets.A.Vida.Secreta.dos.Bichos.2.2019.1080p.BluRay.x264-iFT.DUAL-RK.mkv\"  --default-track 0:yes  --language 1:pt  --track-name \"1:Português\"  --default-track 1:yes  --forced-track 1:yes --output \"G:\\Prontos\\teste\\Pets.A.Vida.Secreta.dos.Bichos.2.2019.1080p.BluRay.x264-iFT.DUAL-RK.mkv.mkv\" \"E:\\filme - pets - a vida secreta dos bixos 2\\Pets.A.Vida.Secreta.dos.Bichos.2.2019.1080p.BluRay.x264-iFT.DUAL-RK\\Pets.A.Vida.Secreta.dos.Bichos.2.2019.1080p.BluRay.x264-iFT.DUAL-RK.mkv\"";
			String command = " \"G:\\CLI\\MkvToolnix x64 v37.0.0\\mkvmerge.exe\" "+" --output \"G:\\Prontos\\teste\\Pets.A.Vida.Secreta.dos.Bichos.2.2019.1080p.BluRay.x264-iFT.DUAL-RK.mkv.mkv\" \"E:\\filmes\\filme - pets - a vida secreta dos bixos 2\\Pets.A.Vida.Secreta.dos.Bichos.2.2019.1080p.BluRay.x264-iFT.DUAL-RK\\Pets.A.Vida.Secreta.dos.Bichos.2.2019.1080p.BluRay.x264-iFT.DUAL-RK.mkv\"";
			
			RuntimeUtils.execSystemCommandCmd(command, new PrintOutputDTO(System.out), "UTF-8");
			
			//https://github.com/profesorfalken/jProcesses
			
//			BufferedReader read = RuntimeUtils.execSystemCommandCmd2(command, "UTF-8");
//			String line = "";
//			StringBuilder str = new StringBuilder();
//			
//			System.out.println(command);
////			str.append(command).append("\n");
//			
//			while ((line = read.readLine()) != null)
//	        {
//				if(!line.contains("%"))
//				{
//					str.append(line).append("\n");
////					updateMessage(str.toString());
//				}
//				else 
//				{
////					updateMessage(str.toString()+line);
//				}
//	        }
//
//			System.out.println(str);
//	        read.close();
		
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
}
