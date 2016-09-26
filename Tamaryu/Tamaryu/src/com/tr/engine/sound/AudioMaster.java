package com.tr.engine.sound;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import com.jogamp.openal.*;
import com.jogamp.openal.util.ALut;

public final class AudioMaster
{
	private static AL al;
	private static ALC alc;
	
	private static boolean initialized = false;
	private static boolean dataLoaded = false;
	
	private static float currentVolume = 1.0f;
	
    private static final int MAX_SOURCES = 30;   
    private static String[] audios = new String[0];
    private static int[] buffers = new int[audios.length];
    private static int[] sources = new int[MAX_SOURCES];

    private static float[][] sourcePos = new float[MAX_SOURCES][3];
    private static float[][] sourceVel = new float[MAX_SOURCES][3];

    private static float[] listenerPos = { 0.0f, 0.0f, 0.0f };
    private static float[] listenerVel = { 0.0f, 0.0f, 0.0f };
    private static float[] listenerOri = { 0.0f, 0.0f, -1.0f, 0.0f, 1.0f, 0.0f };

    private static void loadAllAudios(String[] audioFilePaths)
    {
    	audios = new String[audioFilePaths.length];
    	audios = audioFilePaths;
    	buffers = new int[audios.length];
    }
    
    private static void loadAllBuffers()
    {
    	int[] format = new int[1];
        int[] size = new int[1];
        ByteBuffer[] data = new ByteBuffer[1];
        int[] freq = new int[1];
        int[] loop = new int[1];
                
        if(buffers == null || buffers.length < 1 || buffers.length < audios.length)
        {
        	buffers = new int[audios.length];
        }
        
        for(int i = 0; i < audios.length; i++)
        {
        	if(audios[i] != null && !audios[i].isEmpty())
        	{
        		try{
        			ALut.alutLoadWAVFile(audios[i], format, data, size, freq, loop); 			
        			if(format[0] > 0 && data[0] != null && size[0] > 0 && freq[0] > 0 && size[0] == data[0].limit())
        			{
        				al.alBufferData( buffers[i], format[0], data[0], size[0], freq[0]);
        			}
        		}
        		catch(Exception e)
        		{
        			System.out.println("Error in Loading WAV-File");
        		}
        	}
        }  
    }
    
    private static void loadAllSources()
    {
    	if(sources == null || sources.length < MAX_SOURCES)
        {
        	sources = new int[MAX_SOURCES];
        }
    	
    	System.out.println("Buf Length: "+buffers.length);
    	for(int i = 0; i < buffers.length; i++)
    	{
    		al.alSourcei(sources[i], AL.AL_BUFFER, buffers[i]);
            al.alSourcef(sources[i], AL.AL_PITCH, 1.0f);
            al.alSourcef(sources[i], AL.AL_GAIN, currentVolume);
            al.alSourcefv(sources[i], AL.AL_POSITION, sourcePos[i], 0);
            al.alSourcefv(sources[i], AL.AL_POSITION, sourceVel[i], 0);
            al.alSourcei(sources[i], AL.AL_LOOPING, AL.AL_FALSE);
    	}
    }
    
    private static int loadALData(String[] audioFilePaths)
    {
    	AudioMaster.loadAllAudios(audioFilePaths);
        al.alGenBuffers(audios.length, buffers, 0);
        
        if (al.alGetError() != AL.AL_NO_ERROR)
        {
        	System.out.println("Fehler: " + al.alGetError()); //40964 = AL_INVALID_OPERATION
            return AL.AL_FALSE;
        }
        
        AudioMaster.loadAllBuffers();

        al.alGenSources(MAX_SOURCES, sources, 0);
        AudioMaster.loadAllSources();
        
        if (al.alGetError() != AL.AL_NO_ERROR) 
        {
            return AL.AL_FALSE;
        }
        dataLoaded = true;
        return AL.AL_TRUE;
    }

    private static void setListenerValues()
    {
        al.alListenerfv(AL.AL_POSITION, listenerPos, 0);
        al.alListenerfv(AL.AL_VELOCITY, listenerVel, 0);
        al.alListenerfv(AL.AL_ORIENTATION, listenerOri, 0);
    }
    
    public static void clearData()
    {
    	if(dataLoaded)
    	{
	    	for(int i = 0; i < sources.length; i++)
	    	{
	    		AudioMaster.stopSource(i);
	    		al.alSourcei(sources[i], AL.AL_BUFFER, 0);
	    	}
	    	
	        al.alDeleteBuffers(buffers.length, buffers, 0);
	        
	        buffers = null;
	        audios = null;

	        dataLoaded = false;
    	}
    }
    
    public static void killAllData()
    {
    	if(dataLoaded)
    	{
    		AudioMaster.clearData();
    	}
    	
    	if(initialized)
    	{
	    	al.alDeleteSources(MAX_SOURCES, sources, 0);
	    		    	
	        ALCcontext currentContext = alc.alcGetCurrentContext();
	        ALCdevice currentDevice = alc.alcGetContextsDevice(currentContext);
	    	
	        alc.alcMakeContextCurrent(null);
	    	alc.alcDestroyContext(currentContext);
	        alc.alcCloseDevice(currentDevice);
	        
	        initialized = false;
    	}
    }
    
    public static void loadAudioFiles(String[] audioFilePaths)
    {
    	if(initialized && !dataLoaded)
    	{
	    	if(AudioMaster.loadALData(audioFilePaths) == AL.AL_FALSE)
	        {
	            System.exit(1);
	        }
    	}
    }
    
    public static void initialize()
    {
    	if(!initialized)
    	{
	    	alc = ALFactory.getALC();
	    	
	    	ALCdevice device = alc.alcOpenDevice(null);
	    	ALCcontext context = alc.alcCreateContext(device, null);
	    	alc.alcMakeContextCurrent(context);
	    	    	
	    	al = ALFactory.getAL();
	        al.alGetError();
	        
	        AudioMaster.setListenerValues();
	        initialized = true;
    	}
    }
    
    public static void playSource(int sourceID)
    {
    	if(dataLoaded)
    	{
    		al.alSourcePlay(sources[sourceID]);
       	}
    }
    
    public static void pauseSource(int sourceID)
    {
    	if(dataLoaded)
    	{
    		al.alSourcePause(sources[sourceID]);
    	}
    }
    
    public static void stopSource(int sourceID)
    {
    	if(dataLoaded)
    	{
    		al.alSourceStop(sources[sourceID]);
    	}
    }
    
    public static void setVolume(float volumePercentage)
    {
    	if(volumePercentage >= 0.0f && volumePercentage <= 1.0f)
    	{
    		if(dataLoaded)
    		{
    			currentVolume = volumePercentage;
    			for(int i = 0; i < sources.length; i++)
    	    	{
    				al.alSourcef(sources[i], AL.AL_GAIN, volumePercentage);
    	    	}
    		}
    	}
    }
    
    public static float getVolume()
    {
    	return currentVolume;
    }
    
    public static void setLooping(int sourceID, boolean looping)
    {
    	if(looping)
    	{
    		al.alSourcei(sources[sourceID], AL.AL_LOOPING, AL.AL_TRUE);
    	}
    	else
    	{
    		al.alSourcei(sources[sourceID], AL.AL_LOOPING, AL.AL_FALSE);
    	}
    }
    
    public static boolean isPlaying(int sourceID)
    {
    	IntBuffer state = IntBuffer.allocate(1);
		al.alGetSourcei(sources[sourceID], AL.AL_SOURCE_STATE, state);
		
		return (state.get(0) == AL.AL_PLAYING);
    }
}
