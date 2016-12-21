package alexandvlad.bomjego;

import android.content.Context;
import android.graphics.Bitmap;
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
    private final static int SCALE_ICON = 290;


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

    public Bitmap resizeMapIcons(String iconName, int width, int height){
        Bitmap imageBitmap = BitmapFactory.decodeResource(context.getResources(),context.getResources().getIdentifier(iconName, "drawable", context.getPackageName()));
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, width, height, false);
        return resizedBitmap;
    }

    @Override
    public void onBindViewHolder(BomjeViewHolder holder, int position) {
        final WildBomjeEntry bomje = bomjes.get(position);
        if (bomje.bomje.type.equals(BomjeType.ANTON)) {
            holder.nameView.setText(R.string.bomje_anton);
            holder.imageView.setImageBitmap(resizeMapIcons("bomje_anton",SCALE_ICON,SCALE_ICON));
        } else if (bomje.bomje.type.equals(BomjeType.DELOVOI)) {
            holder.nameView.setText(R.string.bomje_nikita);
            holder.imageView.setImageBitmap(resizeMapIcons("bomje_delovoi",SCALE_ICON,SCALE_ICON));
        } else if (bomje.bomje.type.equals(BomjeType.DEREVENSKI)) {
            holder.nameView.setText(R.string.bomje_vitalya);
            holder.imageView.setImageBitmap(resizeMapIcons("bomje_derevenski",SCALE_ICON,SCALE_ICON));
        } else if (bomje.bomje.type.equals(BomjeType.JIRNIY)) {
            holder.nameView.setText(R.string.bomje_tema);
            holder.imageView.setImageBitmap(resizeMapIcons("bomje_jirniy",SCALE_ICON,SCALE_ICON));
        } else if (bomje.bomje.type.equals(BomjeType.MUTANT)) {
            holder.nameView.setText(R.string.bomje_triple);
            holder.imageView.setImageBitmap(resizeMapIcons("bomje_mutant",SCALE_ICON,SCALE_ICON));
        } else if (bomje.bomje.type.equals(BomjeType.OPASNI)) {
            holder.nameView.setText(R.string.bomje_alex);
            holder.imageView.setImageBitmap(resizeMapIcons("bomje_opasni",SCALE_ICON,SCALE_ICON));
        } else if (bomje.bomje.type.equals(BomjeType.S_BORODOY)) {
            holder.nameView.setText(R.string.bomje_misha);
            holder.imageView.setImageBitmap(resizeMapIcons("bomje_s_borodoy",SCALE_ICON,SCALE_ICON));
        } else if (bomje.bomje.type.equals(BomjeType.SEXY)) {
            holder.nameView.setText(R.string.bomje_glotov);
            holder.imageView.setImageBitmap(resizeMapIcons("bomje_sexy",SCALE_ICON,SCALE_ICON));
        } else if (bomje.bomje.type.equals(BomjeType.SOZDATEL)) {
            holder.nameView.setText(R.string.bomje_creator);
            holder.imageView.setImageBitmap(resizeMapIcons("bomje_sozdatel",SCALE_ICON,SCALE_ICON));
        } else if (bomje.bomje.type.equals(BomjeType.WITH_OGNETUSHITEL)) {
            holder.nameView.setText(R.string.bomje_artem);
            holder.imageView.setImageBitmap(resizeMapIcons("bomje_with_ognetushitel",SCALE_ICON,SCALE_ICON));
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
