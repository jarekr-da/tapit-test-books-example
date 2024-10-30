package com.da.experiment

object TapirConf {
  implicit lazy val customTapirSchemaConfig: sttp.tapir.generic.Configuration =
    sttp.tapir.generic.Configuration.default
      .withDiscriminator("type_discriminator")
}
