package es.rczone.simonsays.tools;

public enum Density {
	
	LDPI(0.75f),
	MDPI(1.0f),
	HDPI(1.5f),
	XHDPI(2.0f),
	XXHDPI(3.0f);
	
	private float density;
	
	Density(float scale){
		this.density = scale;
	}

	public float getDensity(){
		return density;
	}
	
	public static Density falseConstuctor(float density){
		if(density==Density.LDPI.getDensity()){
			return LDPI;
		}
		else if(density==Density.MDPI.getDensity()){
			return MDPI;
		}
		else if(density==Density.HDPI.getDensity()){
			return HDPI;
		}
		else if(density==Density.XHDPI.getDensity()){
			return XHDPI;
		}
		else if(density==Density.XXHDPI.getDensity()){
			return XXHDPI;
		}
		
		return LDPI;
		
	}
	
	public int getSizePicProfile(){
		
		switch(this){
		case LDPI:
		case MDPI:
			return 58;
		case HDPI:
			return 128;
		case XHDPI:
			return 256;
		case XXHDPI:
			return 512;
		default:
			return 58;
		
		}
	}
}
