package com.wouterpot.makeyourshoppinglist.test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.Test;

import com.google.gwt.thirdparty.guava.common.base.Charsets;
import com.google.gwt.thirdparty.guava.common.io.Files;
import com.wouterpot.makeyourshoppinglist.helpers.Resource;

public class ResourceTest {

	@Test
	public void testSplitBlob() throws IOException {
		String content = Files.toString(new File("test/testdata/pages/blob.html"), Charsets.UTF_8);
		List<String> list = Resource.splitBlobByBreak(content);
		assertEquals(10, list.size());
		assertEquals("3 middelgrote courgettes", list.get(0));
		assertEquals("zout en peper", list.get(9));
	}

}
