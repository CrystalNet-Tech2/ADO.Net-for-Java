package ADONet.OracleClient;

import system.Console;
import system.data.oracleclient.*;

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
