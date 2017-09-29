package simpledb;

import java.io.*;
import java.util.*;

/**
 * HeapFile is an implementation of a DbFile that stores a collection of tuples in no particular order. Tuples are
 * stored on pages, each of which is a fixed size, and the file is simply a collection of those pages. HeapFile works
 * closely with HeapPage. The format of HeapPages is described in the HeapPage constructor.
 * 
 * @see simpledb.HeapPage#HeapPage
 */
public class HeapFile implements DbFile {

	/**
	 * The File associated with this HeapFile.
	 */
	protected File file;

	/**
	 * The TupleDesc associated with this HeapFile.
	 */
	protected TupleDesc td;

	/**
	 * Constructs a heap file backed by the specified file.
	 * 
	 * @param f
	 *            the file that stores the on-disk backing store for this heap file.
	 */
	public HeapFile(File f, TupleDesc td) {
		this.file = f;
		this.td = td;
	}

	/**
	 * Returns the File backing this HeapFile on disk.
	 * 
	 * @return the File backing this HeapFile on disk.
	 */
	public File getFile() {
		return file;
	}

	/**
	 * Returns an ID uniquely identifying this HeapFile. Implementation note: you will need to generate this tableid
	 * somewhere ensure that each HeapFile has a "unique id," and that you always return the same value for a particular
	 * HeapFile. We suggest hashing the absolute file name of the file underlying the heapfile, i.e.
	 * f.getAbsoluteFile().hashCode().
	 * 
	 * @return an ID uniquely identifying this HeapFile.
	 */
	public int getId() {
		return file.getAbsoluteFile().hashCode();
	}

	/**
	 * Returns the TupleDesc of the table stored in this DbFile.
	 * 
	 * @return TupleDesc of this DbFile.
	 */
	public TupleDesc getTupleDesc() {
		return td;
	}

	// see DbFile.java for javadocs
	public Page readPage(PageId pid) {
		// some code goes here
		byte [] b=new byte[BufferPool.PAGE_SIZE];
		int pagesize=BufferPool.PAGE_SIZE;
		int ofset=pid.pageno();
		int pos=pagesize*ofset;
		
		RandomAccessFile ram=null;
		
		try {
			ram=new RandomAccessFile(file, "r");
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			ram.seek(pos);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
			ram.readFully(b, 0, BufferPool.PAGE_SIZE);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Page pu = null;
		try {
			//Page p=new HeapPage((HeapPageId) pid, b); 
			pu = new HeapPage((HeapPageId) pid, b);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return pu;

	}

	// throw new UnsupportedOperationException("Implement this");

	// see DbFile.java for javadocs
	public void writePage(Page page) throws IOException {
		// some code goes here
		// not necessary for assignment1
	}

	/**
	 * Returns the number of pages in this HeapFile.
	 */
	public int numPages() {
		return (int) (file.length() / BufferPool.PAGE_SIZE);
	}

	// see DbFile.java for javadocs
	public ArrayList<Page> addTuple(TransactionId tid, Tuple t)
			throws DbException, IOException, TransactionAbortedException {
		// some code goes here
		// not necessary for assignment1
		return null;
	}

	// see DbFile.java for javadocs
	public Page deleteTuple(TransactionId tid, Tuple t) throws DbException, TransactionAbortedException {
		// some code goes here
		// not necessary for assignment1
		return null;
	}

	// see DbFile.java for javadocs
	public DbFileIterator iterator(TransactionId tid) {

		return new DbFileIterator() {

			/**
			 * The ID of the page to read next.
			 */
			int nextPageID = 0;

			/**
			 * An iterator over all tuples from the page read most recently.
			 */
			Iterator<Tuple> iterator = null;

			@Override
			public void open() throws DbException, TransactionAbortedException {
				nextPageID = 0;
				// obtains an iterator over all tuples from page 0
				iterator = ((HeapPage) Database.getBufferPool().getPage(tid, new HeapPageId(getId(), nextPageID++),
						Permissions.READ_WRITE)).iterator();
			}

			@Override
			public boolean hasNext() throws DbException, TransactionAbortedException {
				// some code goes here
				if(iterator==null)
				{
					return false;
				}
				else
				{
					if(iterator.hasNext()==false)
						return false;
					else
						return true;
					
						
				}
				
			
			}

			@Override
			public Tuple next() throws DbException, TransactionAbortedException, NoSuchElementException {
				// some code goes hereTuple res;
				Tuple res;
				
				if(iterator==null)
				{
					throw new NoSuchElementException("No such element");
				}
				
				else
				{
					res= null;
					res=iterator.next();
					return res;
				}
			}
			
			@Override
			public void rewind() throws DbException, TransactionAbortedException {
				close();
				open();
			}

			@Override
			public void close() {
				iterator = null;
			}

		};
	}

}
