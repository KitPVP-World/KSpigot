#!/bin/bash
set -e # Any subsequent(*) commands which fail will cause the shell script to exit immediately

version=$1

if [[ -z "$version" ]]; then
  echo "You need to supply the version argument: $0 <version> (without v, i.e. v1.21.1+2.0.21)"
  exit 0
fi

gh release create "v$version" --generate-notes
git fetch
git checkout "v$version"
./gradlew build publish --stacktrace

printf "> \033[0;32mUploading plugins to github release\033[0mv\n"
gh release upload "v$version" "kotlin-velocity/build/libs/kotlin-velocity-$version-all.jar" "kotlin-paper/build/libs/kotlin-paper-$version-all.jar"


printf "> \033[0;32mCleaning up\033[0mv\n"
git checkout 1.21

printf "\n"
printf "\n"
printf "\033[1;32mPublished new release: \033[0mv%s\n" "$version"
printf "\n"
printf "\n"
