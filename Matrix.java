import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;



public class Matrix {
	
	final static int MOD = 40009;
	
	 //Inmulteste matricele A si B. Operatiile sunt modulo MOD.
    private static int[][] multiplyMatrix(int[][] A, int[][] B) {
    	
        final int N = A.length;
        final int M = B[0].length;
        final int K = A[0].length;

        int[][] Res = new int[N][M];

        for (int i = 0; i < N; ++i) {
        	
            for (int j = 0; j < M; ++j) {
            	
                for (int k = 0; k < K; ++k) {
                	
                    Res[i][j] = (Res[i][j]%MOD + 
                    		(A[i][k]%MOD * B[k][j]%MOD) % MOD) % MOD;
                	
                }
            }
        }

        return Res;
    }

   
    
    //Inmulteste ultima linie a matricii A cu vectorul v
    //Operatiile sunt modulo MOD.
    private static int multiplyMatrixVector(int[][] A, int[] v) {
    	final int N = A.length;
        
    	int x = 0 ;

    	for (int i = 0; i < N; ++i) {
           
                x = ((x % MOD) + (A[N-1][i]%MOD * v[i]%MOD) );
           
        }

        return x;
    }
   
    //Face modulo MOD asupra matricii A
    private static void  modMatrix(int[][] A) {
	   final int N = A.length;
	   
	   for(int i = 0; i < N; i++)
		   
		   for(int j = 0; j < N; j++)
			   
			   A[i][j] = A[i][j] % MOD;
	  
   }
    
  //Ridica la puterea p matricea patratica A. Operatiile sunt modulo MOD.
    private static int[][] powMatrix(int[][] A, int p) {
        int[][] Res = new int[A.length][A.length];

       
        if(p == 1) 
        	return A;
        else{
        	Res = powMatrix(A, p/2);
        	modMatrix(Res);
        	if(p % 2 == 0){
        		modMatrix(multiplyMatrix(Res, Res));
        		return multiplyMatrix(Res, Res);
        	}
        	else{
        		Res = multiplyMatrix(Res, Res);
        		modMatrix(multiplyMatrix(Res,A ));
        		return multiplyMatrix(Res,A );
        	}
        }
        
    }
    
    //ridica numarul x la puterea N
    private static int power(int x, int n) {

    	int res;
       
         if(n == 1) 
         	return x%MOD;
         else {
         	res = power(x%MOD, n/2)%MOD;

         	if(n % 2 == 0){
         		
         		return (res*res)%MOD;
         	}
         	else {
         		
         		return (((res*res)%MOD)*x)%MOD;
         	}
         } 

     }
    /***
     * Metoda read citeste din fisierul "date.in" inputul.
     * @return un vector care contine pe pozitia 0 numarul de linii,
     * 			pe pozitia 1 numarul de coloane ale matricii si pe pozitia 2 
     * 			numarul maxim de valori consecutive care se pot afla pe o coloana
     * 
     */
    
    private static int[] read() {
    	
    	int[] input = new int[3];
    	
    	FileParser textFile = new FileParser("date.in");
		textFile.open();
		int N, M,K;
		
		String word1 = textFile.getNextWord();		
		N = Integer.parseInt(word1);
		
		String word2 = textFile.getNextWord();		
		M = Integer.parseInt(word2);
		
		String word3 = textFile.getNextWord();		
		K = Integer.parseInt(word3);
		
		input[0] = N;
		input[1] = M;
		input[2] = K;
		
		return input;
		
    }
    
    /***
     * Metoda scrie in fisierul "date.out" numarul de cadre care respecta proprietatea
     * ca pe orice coloana se afla cel mult K valori de 1 consecutive
     * 
     * @param frames -numarul de cadre care va fi scris in fisier
     */
    private static void write(int frames) {
    	
		Writer writer = null;

		try {
		    writer = new BufferedWriter(new OutputStreamWriter(
		          new FileOutputStream("date.out")));
		    
		    writer.write(Integer.toString(frames));
		    
		   
		} catch (IOException ex) {
		  
		} finally {
		   try {writer.close();} catch (Exception ex) {}
		}
    }
   
    
    /**
     * Metoda getFrames citeste inputul si determina numarul de cadre care 
     * respecta cerinta problemei.
     */
   public static void getFrames() {
    	int[] input = new int[3];
		input = read();
		
		int N,M,K;
		N = input[0];
		M = input[1];
		K = input[2];
		
		int[][] A = new int[K+1][K+1];
		int[][] Res = new int[K+1][K+1];
		int[] powersOf2 = new int[K+1];
		
		for(int i = 0; i < K ; i++)
			A[i][i+1] = 1;		
	
		for(int i = 0; i < K + 1; i++)
		A[K][i] = 1;
		
		//ridic pe A la N-K
		Res = powMatrix(A, N-K);
	
		//vector linie cu puterile lui 2
		powersOf2[0] = 1;	
		for(int j = 1; j < K+1; j++){
			powersOf2[j] = (2 * powersOf2[j-1] ) % MOD;
		}	
	
		//valoarea obtinuta prin inmultirea ultimei linii a matrcii Fibonacci
		//ridicata la puterea N-K cu vectorul de puteri ale lui 2
		int x = multiplyMatrixVector(Res,powersOf2);	
		
		//numarul de cadre obtinut prin ridicarea la puterea M a valorii
		//obtinute anterior
		int frames = power(x,M);	
	
		write(frames);
    }
   
   /***
	 * Metoda main apeleaza metoda getFrames fara sa stie implementarea
	 * propriu-zisa.
	 * 
	 * @param args
	 */
   
   public static void main(String[] args){ 	
	   
	   getFrames();
    }
    	
    	
}
    
    
