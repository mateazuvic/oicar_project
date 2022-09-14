using System;
using System.Collections.Generic;
using System.Data.SqlClient;
using System.IO;
using System.Linq;
using System.Web;
using System.Windows.Media.Imaging;

namespace DAL.Utils
{
    public class ImageUtils
    {
        public static BitmapImage ByteArrayToBitmapImage(byte[] picture)
        {
            using (var memoryStream = new MemoryStream(picture)) //memorijski tok podataka prima byte[]
            {
                var bitmap = new BitmapImage();

                //kao transakcije
                bitmap.BeginInit();
                bitmap.StreamSource = memoryStream; //convert
                bitmap.CacheOption = BitmapCacheOption.OnLoad; //odmah ga skesiraj kad ga naloadas
                bitmap.EndInit();
                bitmap.Freeze();

                return bitmap;
            }
        }

        public static byte[] BitmapImageToByteArray(BitmapImage image)
        {
            var jpegEncoder = new JpegBitmapEncoder(); // u .jpeg
            jpegEncoder.Frames.Add(BitmapFrame.Create(image)); //dodajemo frame od bitmapa u jpegEncoder
            using (var memoryStream = new MemoryStream())
            {
                jpegEncoder.Save(memoryStream);
                return memoryStream.ToArray();
            }
        }

        public static byte[] ByteArrayFromSqlDataReader(SqlDataReader dr, int column)
        {
            int bufferSize = 1024; //grabilica u koju stane 1024
            byte[] buffer = new byte[bufferSize];
            int currentBytes = 0;
            using (var memoryStream = new MemoryStream())
            {
                using (var binaryWriter = new BinaryWriter(memoryStream)) //decorator pattern
                {
                    int readBytes;
                    do
                    {
                        readBytes = (int)dr.GetBytes(column, currentBytes, buffer, 0, bufferSize); //odi od 0, zagrabi kolko mozes(1024)
                        binaryWriter.Write(buffer, 0, readBytes);
                        currentBytes += readBytes;

                    } while (readBytes == bufferSize); //dok je procitano u jednoj iteraciji 1024 idi dalje (jer to znaci da ima jos)-> 4500 = 1024 + 1024 + 1024 + 1024 + 404
                    return memoryStream.ToArray();
                }
            }
        }
    }
}