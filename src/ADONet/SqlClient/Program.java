package ADONet.SqlClient;

import system.Console;
import system.data.sqlclient.*;

/*
    The code in this example assumes that you can connect to the Northwind sample database on Microsoft SQL Server.
    The code creates a SqlCommand to select rows from the Products table, adding a SqlParameter to restrict the results to
    rows with a UnitPrice greater than the specified parameter value, in this case 5. The SqlConnection is opened inside a
    using block, which ensures that resources are closed and disposed when the code exits. The code executes the command by
    using a SqlDataReader, and displays the results in the console window.
 */
public class Program {
    public static void main(String[] args) {

        String connectionString =
                "Data Source=(local);Initial Catalog=Northwind;"
                        + "Integrated Security=true";

        // Provide the query String with a parameter placeholder.
        String queryString =
                "SELECT ProductID, UnitPrice, ProductName from dbo.products "
                        + "WHERE UnitPrice > @pricePoint "
                        + "ORDER BY UnitPrice DESC;";

        // Specify the parameter value.
        int paramValue = 5;

        // Create and open the connection in a using block. This
        // ensures that all resources will be closed and disposed
        // when the code exits.
        SqlConnection connection = null;
        try {
            connection = new SqlConnection(connectionString);
            try {
                // Create the Command and Parameter objects.
                SqlCommand command = new SqlCommand(queryString, connection);
                SqlParameterCollection sqlParameters = new SqlParameterCollection(command.get_Parameters().getBaseObject());
                sqlParameters.AddWithValue("@pricePoint", paramValue);

                // Open the connection in a try/catch block.
                // Create and execute the DataReader, writing the result
                // set to the console window.
                try {
                    connection.Open();
                    SqlDataReader reader = new SqlDataReader(command.ExecuteReader().getBaseObject());
                    while (reader.Read()) {
                        Console.WriteLine("\t{0}\t{1}\t{2}",
                                reader.get_Item(0), reader.get_Item(1), reader.get_Item(2));
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