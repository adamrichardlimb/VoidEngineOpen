package util;


import java.io.*;

//An interface with a simple method to turn txts into Strings.
public interface StringReader {

    default String readFileAsString(String filename) throws IOException {


        StringBuilder source = new StringBuilder();
        FileInputStream in = new FileInputStream(filename);
        BufferedReader reader;

        try{
            reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));

            try{
                String line;
                while((line = reader.readLine()) != null) {
                    source.append(line).append('\n');
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            finally {

                reader.close();
            }

            in.close();


            }catch(UnsupportedEncodingException e) {
            e.printStackTrace();

        }return source.toString();

    }

}
