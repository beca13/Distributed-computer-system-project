package mapreduce;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import java.util.HashMap;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;

public class ReziseriMinuti {
	
	public static java.util.Map<String, String> trajanjeFilmova = new HashMap<String, String>();

	public static void UcitajHashMapu(String putanja)
	{ 
		String filmovi = "C:\\tmp\\input\\data_filmovi.tsv";
	
		try (BufferedReader reader = new BufferedReader(new FileReader(filmovi));) {
			String lineFilmSaMinutima = reader.readLine(); // preskoci liniju sa zaglavljem
			while ((lineFilmSaMinutima = reader.readLine())!= null) {
				// ucitaj film po film
				String[] elementiNiza = lineFilmSaMinutima.split("\t");
				if (!("\\N".equals(elementiNiza[7]))) {
					String idFilmaIzTabeleMinuti = elementiNiza[0];// id mi treba //
					trajanjeFilmova.put(idFilmaIzTabeleMinuti, elementiNiza[7]);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	System.out.println("Kesiranje u memoriji je uradjeno.");
	}
	
	public static String VratiTrajanjeFilma(String idFilma)
	{		
			return trajanjeFilmova.get(idFilma);
	
	}
	
	public static class Map extends Mapper<LongWritable, Text, Text, IntWritable> {
		private static IntWritable min = new IntWritable(0);
		
		
		private Text word = new Text();		

		@Override
		public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
			// value je jedna linija u fajlu
			String line = value.toString();
			//deli liniju na pojedinacne reci razmakom
			String[] elementiOdvojeniTabom = line.split("\t");			
			String[] reziseri = elementiOdvojeniTabom[1].split(",");
			
			String minuti = VratiTrajanjeFilma(elementiOdvojeniTabom[0]);
			if (minuti==null) {
				//zelim da minuti bude 0
				minuti = "0";
			}
			
			int minutInt = Integer.parseInt(minuti);
			min = new IntWritable(minutInt);
			
			int brojRezisera = reziseri.length;
			for(int i = 0; i< brojRezisera; i++) {
				String reziser = reziseri[i];
				
				// dodeli promenljivoj word rezisera
				word.set(reziser);
				//izbaci rec, 1 kao izlaz iz mepera
				context.write(word, min);
			}
		}
	}

	public static class Reduce extends Reducer<Text, IntWritable, Text, IntWritable> {

		@Override
		public void reduce(Text key, Iterable<IntWritable> values, Context context)
				throws IOException, InterruptedException {
			int sum = 0;
			for (IntWritable value : values) {
				sum += value.get();
			}
			context.write(key, new IntWritable(sum));
		}
	}

	public static void main(String[] args) throws Exception {

		UcitajHashMapu("");		
		
		Job job = Job.getInstance();
		job.setJarByClass(ReziseriMinuti.class);
		job.setJobName("ReziseriMinuti");

		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);

		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);

		job.setMapperClass(Map.class);
		job.setReducerClass(Reduce.class);

		FileInputFormat.setInputPaths(job, new Path("file:///C:/tmp/input/data_film_reziseri_pisci.tsv"));//input moze da bude txt
		FileOutputFormat.setOutputPath(job, new Path("file:///C:/tmp/outR"));//out je folder
		
		
		//podesiti putanju input i output u run configuration
		//FileInputFormat.setInputPaths(job, new Path(args[0]));
		//FileOutputFormat.setOutputPath(job, new Path(args[1]));

		job.waitForCompletion(true);
	}
}
