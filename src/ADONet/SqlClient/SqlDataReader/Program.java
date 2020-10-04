package ADONet.SqlClient.SqlDataReader;

import com.dotNet4Java.api.EClrError;
import system.Console;
import system.data.IDataRecord;
import system.data.sqlclient.SqlCommand;
import system.data.sqlclient.SqlConnection;
import system.data.sqlclient.SqlDataReader;

public class Program {
    public static void main(String[] arg) {
        String str = "Data Source=BIZZO\\SQLEXPRESS;Initial Catalog=Northwind;"
                + "Integrated Security=SSPI";
        ReadOrderData(str);
    }

    private static void ReadOrderData(String connectionString) {
        String queryString =
                "SELECT OrderID, CustomerID FROM dbo.Orders;";

        try {
            SqlConnection connection = null;
            try {
                connection = new SqlConnection(connectionString);
                SqlCommand command = new SqlCommand(queryString, connection);
                connection.Open();

                SqlDataReader reader = command.ExecuteReader().AsType(SqlDataReader.class);

                // Call Read before accessing data.
                while (reader.Read()) {
                    ReadSingleRow(reader.AsType(IDataRecord.class));
                }

                // Call Close when done reading.
                reader.Close();

            } finally {
                if (connection != null) {
                    connection.Dispose();
                    connection.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void ReadSingleRow(IDataRecord record) throws EClrError {
        Console.WriteLine(system.String.Format("{0}, {1}", record.get_Item(0), record.get_Item(10)));
    }
}
