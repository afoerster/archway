package com.heimdali.modules

import java.net.URI

import com.heimdali.services._
import org.apache.hadoop.hdfs.client.HdfsAdmin

trait ClientModule {
  this: AkkaModule with ExecutionContextModule with ConfigurationModule with HttpModule with ContextModule with FileSystemModule =>

  val hdfsUri = new URI("hdfs://cdh.corp.jotunn.io:8020/")

  val hdfsAdmin: HdfsAdmin = new HdfsAdmin(hdfsUri, hadoopConfiguration)

  val ldapClient: LDAPClient = new LDAPClientImpl(configuration) with ActiveDirectoryClient

  val hdfsClient: HDFSClient = new HDFSClientImpl(hadoopFileSystem, hdfsAdmin)

  val yarnClient: YarnClient = new CDHYarnClient(http, configuration)

}
