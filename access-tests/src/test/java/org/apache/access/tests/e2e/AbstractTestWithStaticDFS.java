/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.access.tests.e2e;

import java.io.File;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.hdfs.MiniDFSCluster;
import org.junit.AfterClass;
import org.junit.BeforeClass;

public abstract class AbstractTestWithStaticDFS extends AbstractTestWithStaticHiveServer {

  protected static MiniDFSCluster dfsCluster;
  protected static FileSystem fileSystem;

  @Override
  public Context createContext() throws Exception {
    return new Context(hiveServer, getFileSystem(),
        baseDir, confDir, dataDir, policyFile);
  }

  @Override
  protected FileSystem getFileSystem() {
    return fileSystem;
  }

  @BeforeClass
  public static void setupTestWithDFS()
      throws Exception {
    Configuration conf = new Configuration();
    File dfsDir = assertCreateDir(new File(baseDir, "dfs"));
    conf.set(MiniDFSCluster.HDFS_MINIDFS_BASEDIR, dfsDir.getPath());
    dfsCluster = new MiniDFSCluster.Builder(conf).numDataNodes(2).build();
    fileSystem = dfsCluster.getFileSystem();
  }

  @AfterClass
  public static void tearDownTestWithDFS() throws Exception {
    if(dfsCluster != null) {
      dfsCluster.shutdown();
      dfsCluster = null;
    }
  }
}
