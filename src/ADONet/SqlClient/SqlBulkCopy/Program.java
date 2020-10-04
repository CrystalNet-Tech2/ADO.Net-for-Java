package ADONet.SqlClient.SqlBulkCopy;

//The following console application demonstrates how to load data using the SqlBulkCopy class.
// In this example, a SqlDataReader is used to copy data from the Production.Product table in
// the SQL Server AdventureWorks database to a similar table in the same database.

import system.Console;
import system.data.sqlclient.SqlBulkCopy;
import system.data.sqlclient.SqlCommand;
import system.data.sqlclient.SqlConnection;
import system.data.sqlclient.SqlDataReader;

public class Program {

    public static void main(String[] arg) {
        try {
            String connectionString = GetConnectionString();
            // Open a sourceConnection to the AdventureWorks database.
            SqlConnection sourceConnection = null;
            try {
                sourceConnection = new SqlConnection(connectionString);
                sourceConnection.Open();

                // Perform an initial count on the destination table.
                SqlCommand commandRowCount = new SqlCommand("SELECT COUNT(*) FROM dbo.BulkCopyDemoMatchingColumns;", sourceConnection);
                long countStart = system.Convert.ToInt32(commandRowCount.ExecuteScalar());
                Console.WriteLine("Starting row count = {0}", countStart);

                // Get data from the source table as a SqlDataReader.
                SqlCommand commandSourceData = new SqlCommand(
                        "SELECT ProductID, Name, " +
                                "ProductNumber " +
                                "FROM Production.Product;", sourceConnection);
                SqlDataReader reader = commandSourceData.ExecuteReader().AsType(SqlDataReader.class);

                // Open the destination connection. In the real world you would
                // not use SqlBulkCopy to move data from one table to the other
                // in the same database. This is for demonstration purposes only.
                SqlConnection destinationConnection = new SqlConnection(connectionString);
                try {
                    destinationConnection.Open();

                    // Set up the bulk copy object.
                    // Note that the column positions in the source
                    // data reader match the column positions in
                    // the destination table so there is no need to
                    // map columns.
                    SqlBulkCopy bulkCopy = new SqlBulkCopy(destinationConnection);
                    try {
                        bulkCopy.set_DestinationTableName("dbo.BulkCopyDemoMatchingColumns");

                        try {
                            // Write from the source to the destination.
                            bulkCopy.WriteToServer(reader);
                        } catch (Exception ex) {
                            Console.WriteLine(ex.getMessage());
                        } finally {
                            // Close the SqlDataReader. The SqlBulkCopy
                            // object is automatically closed at the end
                            // of the using block.
                            reader.Close();
                        }
                    } finally {
                        bulkCopy.close();
                    }

                    // Perform a final count on the destination
                    // table to see how many rows were added.
                    long countEnd = system.Convert.ToInt32(commandRowCount.ExecuteScalar());

                    Console.WriteLine("Ending row count = {0}", countEnd);
                    Console.WriteLine("{0} rows were added.", countEnd - countStart);
                    Console.WriteLine("Press Enter to finish.");
                    Console.ReadLine();
                } finally {
                    destinationConnection.Dispose();
                    destinationConnection.close();
                }
            } finally {
                if (sourceConnection != null) {
                    sourceConnection.Dispose();
                    sourceConnection.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // To avoid storing the sourceConnection String in your code,
    // you can retrieve it from a configuration file.
    private static String GetConnectionString() {
        return "Data Source=(local); Integrated Security=true;Initial Catalog=AdventureWorks;";
    }
}
