import glob
import hashlib
import matplotlib.pyplot as plt
import pandas as pd
import re
from datetime import datetime
from matplotlib.patches import Patch


def get_color_from_brand(brand, salt="FlashDim"):
    # override color for brands with similar colors
    if brand == "Motorola":
        brand += 3 * salt
    hex_hash = hashlib.md5((brand + salt).encode()).hexdigest()
    return "#" + hex_hash[:6]


def main(latest_data=""):
    if not latest_data:
        latest_data = max(
            glob.glob("popular_devices_*.csv"),
            key=lambda f: int(re.search(r"popular_devices_(\d{8})\.csv", f).group(1))
            if re.search(r"popular_devices_(\d{8})\.csv", f)
            else 0,
        )
    timestamp_str = re.search(r"popular_devices_(\d{8})\.csv", latest_data).group(1)
    timestamp_dt = datetime.strptime(timestamp_str, "%Y%m%d")
    formatted_date = timestamp_dt.strftime("%d.%m.%Y")
    df = pd.read_csv(latest_data).sort_values(by="installations", ascending=False)

    brands = df["branding"].unique()
    brand_to_color = {brand: get_color_from_brand(brand) for brand in brands}
    colors = df["branding"].map(brand_to_color)

    brand_counts = df["branding"].value_counts()
    sorted_brands = brand_counts.sort_values(ascending=False).index

    plt.style.use("dark_background")
    plt.figure(figsize=(12, 8))
    bars = plt.barh(
        df["gplay-name"],
        df["installations"],
        color=colors,
        edgecolor="none",
        alpha=0.85,
    )

    for bar in bars:
        width = bar.get_width()
        plt.text(
            width + 50,
            bar.get_y() + bar.get_height() / 2,
            f"{int(width)}",
            va="center",
            ha="left",
            color="white",
        )

    plt.xlabel("Number of installations", color="white")
    plt.ylabel("Device", color="white")
    plt.title(
        f"Most used devices (source: Google Play Store - {formatted_date})",
        color="white",
    )

    legend_items = [
        Patch(
            facecolor=brand_to_color[brand],
            edgecolor="none",
            label=f"{brand} ({brand_counts[brand]})",
        )
        for brand in sorted_brands
    ]
    plt.legend(handles=legend_items, loc="lower right", frameon=True)

    plt.gca().invert_yaxis()
    plt.tight_layout()
    plt.savefig(latest_data.split(".")[0] + ".jpg", bbox_inches="tight", dpi=100)
    plt.close()


if __name__ == "__main__":
    main()
