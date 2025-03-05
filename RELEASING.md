Releasing
========

Prerequisites
check `git remote -v` that `OpenAttribution/oa-android-sdk` is there. If you do not see it, add it:
`git remote add upstream https://github.com/OpenAttribution/oa-android-sdk.git`
    
1. Make sure `CHANGELOG.md` is up-to-date on `main` for the impending release. Info under `X.Y.Z` will be used on the GitHub release page.
2. Update `libs.versions.toml` to use `X.Y.Z`
3. Create pull request from your repo to OpenAttribution. Merge pull request to main.
4. Make a new tag: `git tag X.Y.Z`
5. Push tag `git push upstream X.Y.Z` (this triggers the publish to Maven Central)
