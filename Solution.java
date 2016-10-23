import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class Solution {
	
	private int maxDegree = -1;
	private int VertexMax = -1;
	private int[] BinInitialPos = null;
	
	//degreeTable: index is vertex number, value is degree
	int[] degreeTable(HashMap<String, ArrayList<String>> hm){
 		
		int[] degreeTable = new int[VertexMax+1];//1...vertexMax, not use 0;
		
		for (Map.Entry<String, ArrayList<String>> entry : hm.entrySet()) {
		    String key = entry.getKey();
		    ArrayList<String> value = entry.getValue();
		    
		    int vertexDegree = value.size();
		    
		    if(vertexDegree > maxDegree)
		    	maxDegree = vertexDegree;
		    
		    degreeTable[Integer.parseInt(key)] = vertexDegree;
		    //System.out.println(key + value);
		}
		
		return degreeTable;
	}
	
	
	ArrayList<int[]> binSort(int[] degreeTable){
		
		int[] vertTable = new int[VertexMax+1];//1,...,VertexMax
		int[] posTable =  new int[VertexMax+1];
		
		int[] AllBinSize = new int[maxDegree+1];//0,....,maxDegree
		
		for(int start = 1; start < degreeTable.length; start++){
		//for(int degree : degreeTable){
			//the first zero must not be included, it's not a node, the 0 not represent for a degree 0
			//while the others 0 not the first counts.
			AllBinSize[degreeTable[start]]++;
			//AllBinSize[degree]++;
		}
		
		int[] AllBinPos = new int[maxDegree+1];//0,....,maxDegree
		
		AllBinPos[0] = 1;//will use later
		//int allBeforeBins = AllBinSize[0];
		
		for(int i=1; i < AllBinPos.length;i++){
			AllBinPos[i] = AllBinPos[i-1] + AllBinSize[i-1];
		}
		
		BinInitialPos = new int[maxDegree+1];
		//BinInitialPos = AllBinPos;
		System.arraycopy( AllBinPos, 0, BinInitialPos, 0, AllBinPos.length );
		
		for(int vertexId = 1; vertexId < degreeTable.length; vertexId++){
			vertTable[AllBinPos[degreeTable[vertexId]]] = vertexId;// sorted table
			posTable[vertexId] = AllBinPos[degreeTable[vertexId]];
			AllBinPos[degreeTable[vertexId]]++;
		}
		
		ArrayList<int[]> result = new ArrayList<int[]>();
		result.add(vertTable);
		result.add(posTable);
		
		return result;
	}
	
	
	int getMaxCore(HashMap<String, ArrayList<String>> hm){
		int[] degreeTable = degreeTable(hm);
		
		ArrayList<int[]> result = binSort(degreeTable);
		int[] vertTable = result.get(0);
		int[] posTable =  result.get(1);
		
		int maxCore = -1;
		
		for(int start = 1; start < vertTable.length; start++){
			
			if(degreeTable[vertTable[start]] > maxCore)
				maxCore = degreeTable[vertTable[start]];
			
			ArrayList<String> neighbors = hm.get(Integer.toString(vertTable[start]));
			
			if(neighbors!=null){
				for(String neighborString : neighbors){
					if(degreeTable[Integer.parseInt(neighborString)] > degreeTable[vertTable[start]]){
						int originalDegree = degreeTable[Integer.parseInt(neighborString)];
						degreeTable[Integer.parseInt(neighborString)]--;
						//swap in verTable
						int startBin = BinInitialPos[originalDegree];
						int neighborIndex = posTable[Integer.parseInt(neighborString)]; 
						
						int temp = vertTable[startBin];
						vertTable[startBin] = Integer.parseInt(neighborString);
						vertTable[neighborIndex] = temp;
						//swap the position
						posTable[Integer.parseInt(neighborString)] = startBin;
						posTable[temp]=neighborIndex;					
						//update the All BinInitialPos
						BinInitialPos[originalDegree]++;
					}
				}
			}	
			
		}
		
		return maxCore;
	}
	
	
//	ArrayList<Integer> decoupleCores(HashMap<String, ArrayList<String>> hm){
//		
//		ArrayList<Integer> vertexCore = new ArrayList<Integer>();
//		
//		int[] degreeTable = degreeTable(hm);
//		
//		ArrayList<ArrayList<Integer>> twoTables = binSort(degreeTable);
//		
//		ArrayList<Integer> vertTable = twoTables.get(0);
//		ArrayList<Integer> posTable = twoTables.get(1);
//		
//		//int maxcore = 
//		
//		for(int vertexId : vertTable){
//			
//		}
//			
//		
//		
//		return vertexCore;
//		
//	}
	
	
	
	public static void main(String[] args){
		
		Solution run = new Solution();
		
		HashMap<String, ArrayList<String>> hm = new HashMap<String, ArrayList<String>>();
		HashSet<String> hs = new HashSet<String>();
		
		try{
		//BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\ZhengD\\Desktop\\Amazon0601.txt"));
		BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\ZhengD\\Desktop\\Amazon0601.txt"));
		int count = 0;
		
			try {
			    StringBuilder sb = new StringBuilder();
			    String line = br.readLine();
	
			    while (line != null) {
			    	
			    	count++;
			    	
			    	String[] a = line.split("\\t");
			    	
			    	
			    	//System.out.println(line);
			    	
			    	//No nodes 0
//			    	for(int i = 0; i < 2; i++){
//			    		
//			    		if(Integer.parseInt(a[i])>run.VertexMax)
//			    			run.VertexMax = Integer.parseInt(a[i]);
//			    		
//				    	if(hm.containsKey(a[i])){
//				    		hm.get(a[i]).add(a[1-i]);
//				    		//hm.put(a[0], hm.get(a[0]));
//				    	}
//				    	else{
//				    		ArrayList<String> al = new ArrayList<String>();
//				    		al.add(a[1-i]);
//				    		hm.put(a[i], al);
//				    	}
//			    		
//			    	}
			    	
			    	//Exist Nodes 0
			    	if(!hs.contains(line)){
				    	for(int i = 0; i < 2; i++){
				    		
				    		if( Integer.parseInt(a[i])+1 > run.VertexMax)
				    			run.VertexMax = Integer.parseInt(a[i]) + 1;
				    		
					    	if(hm.containsKey(Integer.toString(Integer.parseInt(a[i])+1))){
					    		hm.get(Integer.toString(Integer.parseInt(a[i])+1)).add(Integer.toString(Integer.parseInt(a[1-i])+1));
					    		//hm.put(a[0], hm.get(a[0]));
					    	}
					    	else{
					    		ArrayList<String> al = new ArrayList<String>();
					    		al.add(Integer.toString(Integer.parseInt(a[1-i])+1));
					    		hm.put(Integer.toString(Integer.parseInt(a[i])+1), al);
					    	}
				    		
				    	}
			    	}
			    	
			    	hs.add(line);
			    	hs.add(a[1] + "	" + a[0]);
			    	
			    	if(count % 10000 == 0)
			    		System.out.println(count);
			    	
			        line = br.readLine();
			    }
			    
			    
			} finally {
			    br.close();
			}
		}catch(Exception e){
			System.out.println("Reading com_amazon_ungraph.txt as bufferedReaderhappens");
		}
		
//		for (Map.Entry<String, ArrayList<String>> entry : hm.entrySet()) {
//		    String key = entry.getKey();
//		    ArrayList<String> value = entry.getValue();
//		    System.out.println(key + value);
//		}
		
		//System.out.println(hm.get("1"));
		
//Test the degreeTable function	
		

//		int[] out = run.degreeTable(hm);
//		int id = 0;
//		for(int degree : out){
//			if(degree!=0){
//				System.out.println(id+":"+degree);
//			}
//			id++;
//		}
//		
//		System.out.println("========================");
//		
//		ArrayList<int[]> result = run.binSort(out);
//			
//		int[] vertTable = result.get(0);
//		int[] posTable =  result.get(1);
//		
//		for(int index=1; index<vertTable.length; index++){
//		//for(int vertext : vertTable){
//			if(vertTable[index]!=0){
//				System.out.println(index+":"+vertTable[index]);
//			}
//			
//		}
//		
//		System.out.println("========================");
//		
//		for(int index=1; index<posTable.length; index++){
//		//for(int vertext : vertTable){
//			if(posTable[index]!=0){
//				System.out.println(index+":"+posTable[index]);
//			}
//			
//		}
//		
//		System.out.println("========================");
		
		int resultCore = run.getMaxCore(hm);
		System.out.println("MaxCore: " + resultCore);
		
	}
}

