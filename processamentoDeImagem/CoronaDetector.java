package processamentoDeImagem;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import javax.imageio.ImageIO;

public class CoronaDetector {
	
	BufferedImage image;
	
	int width;
	
	int height;
	
	public ArrayList<int[]> detector(String path) {
		try {
			
			File input = new File(path);
			image = ImageIO.read(input);
			width = image.getWidth();
			height = image.getHeight();
			
			ArrayList<int[]>selection = new ArrayList<>();
			
			//empilhamento dos candidatos
			for(int i=0; i<height; i++){
				for(int j=0; j<width; j++){
					
					Color c = new Color(image.getRGB(j, i));
					int r = c.getRed();
					int g = c.getGreen();
					int b = c.getBlue();
					
					if (r+g+b>700) {
						Color newColor = new Color(255,0,0);
						image.setRGB(j,i,newColor.getRGB());						
												
						int[] coordinates = new int[3];
						coordinates[0] = i;
						coordinates[1] = j;						
						coordinates[2] = (int) Math.sqrt(Math.pow(i,2) + Math.pow(j,2));
						selection.add(coordinates);
					}																
				}
			}//fim do for			
			//determinação das posições	dos clusters		
			
			ArrayList<int[]>locations = new ArrayList<>();
						
			while(selection.size()>0) {
				int count = 0;
				
				for(int aux = 1; aux < selection.size(); aux++) {
								
					if(Math.abs(selection.get(0)[2] - selection.get(aux)[2]) < 30) {
												
						selection.remove(aux);
						count++;
						aux--;						
					}
				}
				
				if (count > 50) {					
					int[] positions = new int[2];
					positions[0] = selection.get(0)[0];
					positions[1] = selection.get(0)[1];						
					locations.add(positions);
					selection.remove(0);
				}
				else {
					selection.remove(0);
				}				
			}//fim do for z
			
			return locations;
					
		} catch (Exception e) {
			System.out.println("Possível problema no carregamento da imagem!");
			System.out.println(e);
			ArrayList<int[]>falha = new ArrayList<>();
			int[] f = new int[2];
			f[0] = 777;
			f[1] = 777;
			falha.add(f);
			return falha;
			}		
	}
	static public void main(String args[]) throws Exception{
		
		String path2 = "/home/siad-aero/workspace/CoronaDetector6/src/processamentoDeImagem/telhado2.jpg";
		CoronaDetector obj = new CoronaDetector();
		ArrayList<int[]>result = obj.detector(path2);
		for(int[] aux:result) {
			System.out.println(Arrays.toString(aux));			
		}		
	}
}