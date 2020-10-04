package ADONet.OracleClient;

import system.Console;
import system.data.oracleclient.*;

/*
The code in this example assumes a connection to DEMO.CUSTOMER on an Oracle server. You must also add a reference to the System.Data.OracleClient.dll. The code returns the data in an OracleDataReader.
 */
public class Program {
    public static void main(String[] args) {

        String connectionString =
                "Data Source=ThisOracleServer;Integrated Security=yes;";
        String queryString =
                "SELECT CUSTOMER_ID, NAME FROM DEMO.CUSTOMER";

        try {
            OracleConnection connection = new OracleConnection(connectionString);
            try {
                OracleCommand command = new OracleCommand(connection.CreateCommand().getBaseObject());
                command.set_CommandText(queryString);

                try {
                    connection.Open();

                    OracleDataReader reader = new OracleDataReader(command.ExecuteReader().getBaseObject());

                    while (reader.Read()) {
                        Console.WriteLine("\t{0}\t{1}",
                                reader.get_Item(0), reader.get_Item(1));
                    }
                    reader.Close();
                } catch (Exception ex) {
                    Console.WriteLine(ex.getMessage());
                }
                Console.ReadLine();

            } finally {
                connection.Dispose();
                connection.close();
            }
        } catch (Exception error) {
            error.printStackTrace();
        }
    }
}
