package alexandvlad.bomjego;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import alexandvlad.bomjego.model.BomjeType;
import alexandvlad.bomjego.model.WildBomjeEntry;

class WildBomjeEntryAdapter extends RecyclerView.Adapter<WildBomjeEntryAdapter.BomjeViewHolder> {

    private static final String TAG = "WildBomjeEntryAdapter";
    private final Context context;
    private final LayoutInflater layoutInflater;
    private List<WildBomjeEntry> bomjes;

    WildBomjeEntryAdapter(Context context) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    void setBomje(List<WildBomjeEntry> bomjes) {
        this.bomjes = bomjes;
        notifyDataSetChanged();
    }

    @Override
    public BomjeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return BomjeViewHolder.newInstance(layoutInflater, parent);
    }

    @Override
    public void onBindViewHolder(BomjeViewHolder holder, int position) {
        final WildBomjeEntry bomje = bomjes.get(position);
        if (bomje.bomje.type.equals(BomjeType.NORMAL)) {
            holder.nameView.setText("NORMAL BOMJE");
            holder.imageView.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.bomje1));
        } else if (bomje.bomje.type.equals(BomjeType.WITH_BOX)) {
            holder.nameView.setText("BOMJE IN BOX");
            holder.imageView.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.bomje2));
        } else if (bomje.bomje.type.equals(BomjeType.RAIL_STATION)) {
            holder.nameView.setText("RAIL STATION BOMJE");
            holder.imageView.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.bomje3));
        } else if (bomje.bomje.type.equals(BomjeType.PARK)) {
            holder.nameView.setText("PARK BOMJE");
            holder.imageView.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.bomje4));
        }
        holder.weightView.setText("Weight: " + bomje.bomje.weight);
        holder.heightView.setText("Height: " + bomje.bomje.height);
    }

    @Override
    public int getItemCount() {
        return bomjes.size();
    }

    static class BomjeViewHolder extends RecyclerView.ViewHolder {

        final TextView nameView;
        final ImageView imageView;
        final TextView weightView;
        final TextView heightView;

        BomjeViewHolder(View itemView) {
            super(itemView);
            nameView = (TextView) itemView.findViewById(R.id.bomje_name);
            imageView = (ImageView) itemView.findViewById(R.id.bomje_image);
            weightView = (TextView) itemView.findViewById(R.id.bomje_weight);
            heightView = (TextView) itemView.findViewById(R.id.bomje_height);
        }

        static BomjeViewHolder newInstance(LayoutInflater layoutInflater, ViewGroup parent) {
            final View view = layoutInflater.inflate(R.layout.item_bomje, parent, false);
            return new BomjeViewHolder(view);
        }
    }

}
