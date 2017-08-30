package rf3;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class Utility
{
	/**
	 * 复制数据
	 * @param src
	 * @param dest
	 */
	public static void copyList(List<DataItem> src, List<DataItem> dest)
	{
		dest.clear();
		for(DataItem di : src)
			dest.add(di);
	}
	
	/**
	 * 加载数据，以空格分隔
	 * @param dataFile
	 * @return
	 */
	public static List<DataItem> loadData(String dataFile)
	{
		List<DataItem> dataList = new ArrayList<DataItem>();
		try
		{
			BufferedReader br = new BufferedReader(new FileReader(dataFile));
			String line = "";
			while((line = br.readLine()) != null)
			{
				if(line.trim().length() < 2)
					continue;
				DataItem di = new DataItem();
				String [] items = line.split(" ");
				di.setFeatureCount(items.length - 1);
				for(int i = 0; i < items.length - 1; i++)
				{
					double feature = Double.parseDouble(items[i]);
					di.setFeature(i, feature);
				}
				
				int label = Integer.parseInt(items[items.length - 1]);
				di.setLabel(label);
				
				dataList.add(di);
			}
			br.close();
			
			
		}
		catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return dataList;
		
		
		
	}
	
}
