# SkyStrike (Fabric 1.21.1)

Client-side cosmetic sky lightning prototype. Press **G** to trigger the effect.

## Build without installing anything locally

This repository includes a GitHub Actions workflow at `.github/workflows/build.yml`.
After uploading the project to a GitHub repository, open **Actions → Build SkyStrike → Run workflow** (or push to `main`). When the run completes, open the run and download the `skystrike-1.0.0` artifact. It contains the compiled JAR.

Requirements for Minecraft: Java Edition 1.21.1, Fabric Loader, Java 21.

Note: this is a prototype using Minecraft's cosmetic lightning entity for the visual. Appearance depends on the game renderer and it is not a custom photorealistic ZickZackV6 renderer.
