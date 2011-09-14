package nz.co.nzc.networkutils.reader;
/*
 * This file is part of capacitydb.
 *
 * capacitydb is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * capacitydb is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.log4j.Logger;


public class ExtractArchive {
	
	private static Logger jlog = Logger.getLogger("nz.co.nzc.capacitydb.parser.ExtractArchive");


	private static final int BUFFER = 2048;
	//private static final int MIN_FILE_SIZE = 10;
	//private static final int MAX_UNGZIP_RETRIES = 10;
	
	protected List<File> zipped;
	
	public ExtractArchive(){
		this.zipped = new ArrayList<>();
	}
	
	public static void extract(String zfn) {
		File f = new File(zfn);
		try {
			unzip(f);
		}
		catch (FileNotFoundException fnfe){
			System.err.println("Source file/data not avaliable "+fnfe);
			System.exit(1);
		}
		//return f1;
	}
	/**
	 * Top unzip method.
	 */
	protected static void unzip(File zipfile) throws FileNotFoundException {
		File path = zipfile.getParentFile();
		try {
			ZipArchiveInputStream zais = new ZipArchiveInputStream(new FileInputStream(zipfile));
			ZipArchiveEntry z1 = null;
			while ((z1 = zais.getNextZipEntry())!=null){
				String fn = z1.getName();
				if(fn.contains("/")){
					fn = fn.substring(z1.getName().lastIndexOf("/"));
				}
				File f = new File(path+File.separator+fn);
				FileOutputStream fos = new FileOutputStream(f);
				BufferedOutputStream bos = new BufferedOutputStream(fos, BUFFER);


				int n = 0;
				byte[] content = new byte[BUFFER];
				while (-1 != (n = zais.read(content))) {
				    fos.write(content, 0, n);
				}

				bos.flush();
				bos.close();
				fos.close();
			}
			
			zais.close();
			zipfile.delete();
		}
		catch(IOException ioe){
			jlog.fatal("IO read error :: "+ioe);
		}


	}

	/**
	 * The nested unzip method. Given a zip stream, decompress and store in file in temp_dir.
	 * Replaced by unzip3.
	 */
	/*
	protected File unzip2(File zf) throws FileNotFoundException {
		File f = null;
		String rename = "notused";//zf.getAbsolutePath().replaceFirst("\\.zip", identifier+".xml");//.replaceFirst("\\.gz", ".xml");
		//File f = new File(rename);
		try {
			FileInputStream fis = new FileInputStream(zf);
			FileOutputStream fos = new FileOutputStream(rename);
			ZipInputStream zin = new ZipInputStream(fis);
			final byte[] content = new byte[BUFFER];
			int n = 0;
			while (-1 != (n = zin.read(content))) {
			    fos.write(content, 0, n);
			}

			fos.flush();
			fos.close();

			fis.close();
			zin.close();
		}
		catch (IOException ioe) {
			jlog.error("Error processing Zip "+zf+" Excluding! :: "+ioe);
			return null;
		}
		//try again... what could go wrong
		if (checkMinFileSize(f) && retry_counter<MAX_UNGZIP_RETRIES){
			retry_counter++;
			f.delete();
			f = unzip2(zf);
		}
		
		return f;
	}
	*/

	/**
	 * extract tarfile to constituent parts processing gzips along the way
	 * yyyyMMdd.tar->/yyyyMMdd/INode-CH_RNC01/A2010...gz
	 */
	/*
	protected void untar(File tf) throws FileNotFoundException {

		try {
			TarArchiveInputStream tais = new TarArchiveInputStream(new FileInputStream(tf));
			TarArchiveEntry t1 = null;
			while ((t1 = tais.getNextTarEntry())!=null){
				
				String fn = t1.getName().substring(t1.getName().lastIndexOf("/"));
				File f = new File(getCalTempPath()+fn);
				FileOutputStream fos = new FileOutputStream(f);
				BufferedOutputStream bos = new BufferedOutputStream(fos, BUFFER);


				int n = 0;
				byte[] content = new byte[BUFFER];
				while (-1 != (n = tais.read(content))) {
				    fos.write(content, 0, n);
				}

				bos.flush();
				bos.close();
				fos.close();

				File unz = null;
				if (f.getName().endsWith("zip"))
					unz = unzip3(f);
				else unz = ungzip(f);

				if(unz!=null)
					zipped.add(unz);
				f.delete();
			}
			
			tais.close();
		}
		catch(IOException ioe){
			jlog.fatal("IO read error :: "+ioe);
		}


	}
	*/

	/**
	 * ungzip. Given a gzip stream, decompress and store in file in temp_dir
	 */
	/*
	protected File ungzip(File gzf) throws FileNotFoundException {
		//File f = null;
		String rename = gzf.getAbsolutePath().replaceFirst("\\.gz", identifier+".xml");
		File f = new File(rename);
		try {
			FileInputStream fis = new FileInputStream(gzf);
			FileOutputStream fos = new FileOutputStream(rename);
			GzipCompressorInputStream gzin = new GzipCompressorInputStream(fis);
			final byte[] content = new byte[BUFFER];
			int n = 0;
			while (-1 != (n = gzin.read(content))) {
			    fos.write(content, 0, n);
			}

			fos.flush();
			fos.close();

			fis.close();
			gzin.close();
		}
		catch (IOException ioe) {
			jlog.error("Error processing GZip "+gzf+" Excluding! :: "+ioe);
			return null;
		}
		//try again... what could go wrong
		if (checkMinFileSize(f) && retry_counter<MAX_UNGZIP_RETRIES){
			retry_counter++;
			f.delete();
			f = ungzip(gzf);
		}
		return f;
	}
	 */
	/*
	private boolean checkMinFileSize(File f){
		if (f.length()<MIN_FILE_SIZE) return true;
		return false;
	}
	*/
}
