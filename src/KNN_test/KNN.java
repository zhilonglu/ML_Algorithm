package KNN_test;
import java.util.ArrayList;  
import java.util.Collections;  
import java.util.Comparator;  
import java.util.HashMap;  
import java.util.List;  
import java.util.Map;  
public class KNN {  
	public double calDistance(List<Double> d1, List<Double> d2) {  
		double distance = 0.00;  
		for (int i = 0; i < d1.size(); i++) {  
			distance += (d1.get(i) - d2.get(i)) * (d1.get(i) - d2.get(i));  
		}  
		return distance;  
	}  
	public double knn(List<List<Double>> datas, List<Double> testData, int k) { 
		HashMap<String,Double> dis_value = new HashMap<String,Double>();
		for (int i = 0; i < datas.size(); i++) {  
			List<Double> currData = datas.get(i);  
			double value = currData.get(currData.size() - 1);  
			String key = String.valueOf(i)+"#"+value;
			double distance = calDistance(testData, currData);
			dis_value.put(key,distance);  
		}  
		List<Map.Entry<String, Double>> list_Data = new ArrayList<Map.Entry<String, Double>>(dis_value.entrySet());    
		Collections.sort(list_Data, new Comparator<Map.Entry<String, Double>>()    
				{     
			public int compare(Map.Entry<String, Double> o1, Map.Entry<String, Double> o2)    
			{    
				if(o2.getValue()!=null&&o1.getValue()!=null&&o2.getValue().compareTo(o1.getValue())>0){    
					return -1;    
				}else{    
					return 1;    
				}    
			}
				}); 
		double value = 0;
		for (int i = 0; i < k; i++){  
			double c = Double.valueOf(list_Data.get(i).getKey().split("#")[1]);
			value += 1/c;
		}  
		return k/value;  
	}  
}  