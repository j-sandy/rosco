/*
 * Copyright 2015 Netflix, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.netflix.spinnaker.rosco.api

import com.fasterxml.jackson.annotation.JsonProperty
import com.google.gson.annotations.SerializedName
import com.netflix.spinnaker.kork.artifacts.model.Artifact
import com.netflix.spinnaker.rosco.providers.util.PackageUtil
import com.netflix.spinnaker.rosco.providers.util.packagespecific.DebPackageUtil
import com.netflix.spinnaker.rosco.providers.util.packagespecific.NupkgPackageUtil
import com.netflix.spinnaker.rosco.providers.util.packagespecific.RpmPackageUtil
import groovy.transform.CompileStatic
import groovy.transform.Immutable
import io.swagger.v3.oas.annotations.media.Schema

/**
 * A request to bake a new machine image.
 *
 * @see BakeryController#createBake(String, BakeRequest, String)
 */
@Immutable(copyWith = true)
@CompileStatic
class BakeRequest {

  // A generated uuid which will identify the request and be used as the jobId when running the bake
  @Schema(description= "A generated UUID which will be used to identify the effective packer bake", accessMode = Schema.AccessMode.READ_ONLY)
  String request_id = UUID.randomUUID().toString()
  String user
  @Schema(description="The package(s) to install, as a space-delimited string") @JsonProperty("package") @SerializedName("package")
  String package_name
  @Schema(description="The package(s) to install, as Spinnaker artifacts")
  List<Artifact> package_artifacts
  @Schema(description="The CI server")
  String build_host
  @Schema(description="The CI job")
  String job
  @Schema(description="The CI build number")
  String build_number
  @Schema(description="The commit hash of the CI build")
  String commit_hash
  @Schema(description="The CI Build Url")
  String build_info_url
  @Schema(description="The target platform")
  CloudProviderType cloud_provider_type
  Label base_label
  @Schema(description="The named base image to resolve from rosco's configuration")
  String base_os
  String base_name
  @Schema(description="The explicit machine image to use, instead of resolving one from rosco's configuration")
  String base_ami
  VmType vm_type
  StoreType store_type
  Boolean enhanced_networking
  String ami_name
  String ami_suffix
  Boolean upgrade
  String instance_type
  @Schema(description="The image owner organization")
  String organization

  @Schema(description="The explicit packer template to use, instead of resolving one from rosco's configuration")
  String template_file_name
  @Schema(description="A map of key/value pairs to add to the packer command")
  Map extended_attributes
  @Schema(description="The name of a json file containing key/value pairs to add to the packer command (must be in the same location as the template file)")
  String var_file_name

  @Schema(description="The name of a configured account to use when baking the image")
  String account_name

  String spinnaker_execution_id

  String publisher
  String offer
  String sku

  OsType os_type
  PackageType package_type

  String custom_managed_image_name

  static enum CloudProviderType {
    alicloud, aws, azure, docker, gce, huaweicloud, oracle, tencentcloud
  }

  static enum Label {
    release, candidate, previous, unstable, foundation
  }

  static enum PackageType {
    RPM(new RpmPackageUtil()),
    DEB(new DebPackageUtil()),
    NUPKG(new NupkgPackageUtil())

    private final PackageUtil util

    private PackageType(PackageUtil util) {
      this.util = util
    }

    PackageUtil getUtil() {
      return util
    }
  }

  static enum VmType {
    pv, hvm
  }

  static enum StoreType {
    ebs, s3, docker
  }

  static enum OsType {
    linux, windows
  }
}

