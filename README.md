# Spray-Json-Optics

[Monocle](https://www.optics.dev/Monocle) binding for [Spray-json](https://github.com/spray/spray-json), heavily
inspired by [Circe-optics](https://github.com/circe/circe-optics).

## Using spray-json-optics

```scala
libraryDependencies ++= Seq(
  "io.github.greyplane" %% "spray-json-optics" % version
)
```

## Warning

unfortunately because the simple encoding for numeric value that spray json was using,
it's either impossible or overcomplicated to make these numeric prism lawful, it is still useful but should be used with
caution.