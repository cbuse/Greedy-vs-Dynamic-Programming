import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;


public class Sauron {
	
	
	/***
	 * Metoda read citeste din fisierul "date.in" stapanii si sclavii si ii distribuie
	 * in 2 liste.
	 * 
	 * Mentionez ca am preluat FileParser din prima tema de la POO. Acesta 
	 * facea parte din scheletul de cod, deci nu e creatie proprie. Am facut
	 * cateva modificari pentru a-l putea folosi si aici.
	 * 
	 * @return o lista care contine pe pozitia 0 lista cu stapanii cititi
	 * 			din fisier si pe pozitia 1 lista cu sclavii cititi din fisier
	 */
	
	private static ArrayList<ArrayList<Integer>> read(){
		
		ArrayList<ArrayList<Integer>> initial_pairs = 
				new ArrayList<ArrayList<Integer>>();
		
		ArrayList<Integer> masters = new ArrayList<Integer>();
		ArrayList<Integer> slaves = new ArrayList<Integer>();
		
		String word;

		FileParser textFile = new FileParser("date.in");
		textFile.open();
		
		word = textFile.getNextWord();
		
		 
		boolean ok = false;
		
		while ((word = textFile.getNextWord()) != null) {
			//daca am citit un stapan
			if(ok == false) {
				
				masters.add(Integer.parseInt(word));			
				ok = true;
			}
			//daca am citit un sclav
			else if(ok == true) { 
				
			slaves.add(Integer.parseInt(word));
			ok = false;
			}				
		}
		
		initial_pairs.add(masters);
		initial_pairs.add(slaves);
		return initial_pairs;
	}
	
	/****
	 * Metoda pair gaseste perechile de indecsi cu stapani si cu 
	 * cu sclavi pe care ii va plati Sauron.
	 * 
	 * @param masters - valorile initiale ale stapanilor
	 * @param slaves - valorile initiale ale sclavilor
	 * @return - lista cu indecsii stapinilor alesi, precum si lista indecsilor
	 * 			 sclavilor alesi
	 */
	private static ArrayList<ArrayList<Integer>> pair(ArrayList<Integer> masters,
			ArrayList<Integer> slaves) {		
		
		ArrayList<Integer> index_masters = new ArrayList<Integer>();
		ArrayList<Integer> index_slaves = new ArrayList<Integer>();
		
		//lista indexes va contine 2 liste: cea cu indecsii stapanilor
		//(pe pozitia 0) si cea cu indecsii sclavilor(pe pozitia 1)
		ArrayList<ArrayList<Integer>> indexes = 
				new ArrayList<ArrayList<Integer>>();
		
		//initializarea
		index_masters.add(1);
		index_slaves.add(0);		
		
		
		int pos = -1;
		
		//parcurg toti stapanii
		for(int i = 2; i < masters.size() - 1; i += 2) {
			
			int sum = Integer.MIN_VALUE;
			//nicio imperechere cu vreunul dintre stapanii alesi deja nu e mai buna
			boolean ok = false;
			
			//parcurg toti stapanii alesi pana la pasul i
			//pentru a vedea daca gasesc o imperechere mai buna
			//pentru sclavul curent
			for(int j = 0; j < index_masters.size(); j++) {
				
				//folosirea criteriului de optim 1
				if(slaves.get(i) + masters.get(index_masters.get(j)) >=
				masters.get(i) + slaves.get(index_masters.get(j))) {					
			
					int s = slaves.get(i) + masters.get(index_masters.get(j)) - 
					masters.get(i) - slaves.get(index_masters.get(j));
					
					//folosirea criteriului de optim 2
					if (s >= sum){
						sum = s;
						//voi pastra pozitia in pos pozitia stapanului
						//care va deveni sclav
						pos = j;
					}
					//am gasit o imperechere mai buna pentru sclavul curent
					ok = true;
				}
			
			}
			
			
			if (ok == false) {
				
				index_slaves.add(i);
				
			}
			else {
				
			index_slaves.add(index_masters.get(pos));
			index_masters.set(pos,i);
			}
			
			//continuitate
			index_masters.add(i+1);
			
		}
		
		indexes.add(index_masters);
		indexes.add(index_slaves);
		
		return indexes;
		
	}
	
	/***
	 * Metoda calculeaza salariul minim pe baza indecsilor stapanilor si ai 
	 * sclavilor alesi.
	 * 
	 * @param masters - valorile initiale ale stapanilor
	 * @param slaves - valorile initiale ale sclavilor
	 * @param index_masters - indecsii stapanilor alesi 
	 * @param index_slaves - indecsii scalvilor alesi
	 * @return sariul minim 
	 */
	private static int sum(ArrayList<Integer> masters,
							ArrayList<Integer> slaves,
							ArrayList<Integer> index_masters,
							ArrayList<Integer> index_slaves) {
		
		int min_sum = 0;
		
		for(int i = 0 ; i < index_masters.size(); i++){
			min_sum = min_sum + masters.get(index_masters.get(i)) +
					slaves.get(index_slaves.get(i));
		}
		return min_sum;
	}
	

	/**
	 * Metoda write scrie in fisierul "date.out" salariul minim, precum si
	 * indecsii stapanilor si ai sclavilor alesi(care vor fi platiti) majorati cu 1
	 * 
	 * @param min_sum - salariul minim
	 * @param index_masters - valorile stapanilor care vor fi platiti
	 * @param index_slaves - valorile sclavilor care vor fi platiti
	 */
	
	private static void write(int min_sum,ArrayList<Integer> index_masters,
			ArrayList<Integer> index_slaves) {
		
		Writer writer = null;

		try {
		    writer = new BufferedWriter(new OutputStreamWriter(
		          new FileOutputStream("date.out")));
		    
		    writer.write(Integer.toString(min_sum));
		    writer.write("\r\n");
		    for(int i = 0; i < index_masters.size();i++){
		    	int master_index = index_masters.get(i);
		    	master_index++;
		    	writer.write(Integer.toString(master_index));
		    	writer.write(" ");
		    	int slave_index = index_slaves.get(i);
		    	slave_index++;
		    	writer.write(Integer.toString(slave_index));
		    	 writer.write("\r\n");
		    	
		    }
		   
		} catch (IOException ex) {
		  
		} finally {
		   try {writer.close();} catch (Exception ex) {}
		}
	}
	
	/***
	 * Metoda salarySauron citeste din fisier valorile initiale ale stapanilor
	 * si ale sclavilor, ii imperecheaza astfel incat Sauron sa le plateasca un 
	 * salariu minim si scrie in fisier salariul minim si indecsii stapanilor si
	 * scalavilor care vor fi platiti. 
	 *  
	 */
	public static void salarySauron() {
		ArrayList<Integer> masters = new ArrayList<Integer>();
		ArrayList<Integer> slaves = new ArrayList<Integer>();
		
		ArrayList<Integer> index_masters = new ArrayList<Integer>();
		ArrayList<Integer> index_slaves = new ArrayList<Integer>();
		
		ArrayList<ArrayList<Integer>> indexes = 
				new ArrayList<ArrayList<Integer>>();
		
		masters = read().get(0);
		slaves = read().get(1);
		
		indexes = pair(masters,slaves);
		index_masters = indexes.get(0);
		index_slaves = indexes.get(1);
		
		
		int min_sum = sum(masters,slaves,index_masters,index_slaves);
		
		write(min_sum,index_masters,index_slaves);
		
	}
	
	/***
	 * Metoda main apeleaza metoda salarySauron si rezolva problema lui Sauron
	 * fara sa stie implementarea propriu-zisa.
	 * @param args
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static void main(String[] args) throws FileNotFoundException, 
													IOException {		
		
		salarySauron();	
		
	}
}
