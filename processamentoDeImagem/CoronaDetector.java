package processamentoDeImagem;

import java.awt.Color;
//import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import org.opencv.core.Point;

public class CoronaDetector {
	
	BufferedImage image;
	
	int width;
	
	int height;
	
	public List<Point> detector(String path) {
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
		
			List<Point> lstPoints = new ArrayList<Point>();
						
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
					
					lstPoints.add(new Point(selection.get(0)[0],selection.get(0)[1]));					
					selection.remove(0);
				}
				else {
					selection.remove(0);
				}				
			}//fim do for z
			
			return lstPoints;
					
		} catch (Exception e) {
			System.out.println("Possível problema no carregamento da imagem!");
			System.out.println(e);
			List<Point> falha = new ArrayList<Point>();
			Point pt = new Point();
			pt.x = 777;
			pt.y = 777;
			falha.add(pt);			
			return falha;
			}		
	}
	static public void main(String args[]) throws Exception{
		
		String path2 = "/home/siad-aero/workspace/CoronaDetector6/src/processamentoDeImagem/corona-4.jpg";
		CoronaDetector obj = new CoronaDetector();	
		List<Point> resultado = obj.detector(path2);
		
		for(Point aux:resultado){
			System.out.println(aux);
		}		
	}
}