package com.yottamobile.doraemon.object;

import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.vbo.ISpriteVertexBufferObject;
import org.andengine.entity.text.Text;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class PikaMission extends Sprite {

    public PikaMission(float pX, float pY, float pWidth, float pHeight, ITextureRegion pTextureRegion,
                       ISpriteVertexBufferObject pSpriteVertexBufferObject) {
        super(pX, pY, pWidth, pHeight, pTextureRegion, pSpriteVertexBufferObject);
        // TODO Auto-generated constructor stub
    }

    public PikaMission(int pX, int pY, TextureRegion textureRegion, VertexBufferObjectManager vertexBufferObjectManager) {
        super(pX, pY, textureRegion, vertexBufferObjectManager);
    }

    public PikaMission(int pX, int pY, int pIKA_SIZE, int pIKA_SIZE2, TextureRegion textureRegion, VertexBufferObjectManager vertexBufferObjectManager) {
        super(pX, pY, pIKA_SIZE, pIKA_SIZE2, textureRegion, vertexBufferObjectManager);
    }

    public PikaMission(float pX, float pY, int pIKA_SIZE, int pIKA_SIZE2, TextureRegion pTextureRegion,
                       VertexBufferObjectManager vertexBufferObjectManager) {
        super(pX, pY, pIKA_SIZE, pIKA_SIZE2, pTextureRegion, vertexBufferObjectManager);
    }

    public PikaMission(float x, float y, TextureRegion textureRegion, VertexBufferObjectManager vertexBufferObjectManager) {
        super(x, y, textureRegion, vertexBufferObjectManager);
    }

    public int count, type;
    public Text mText;

    // float x, y;

    public void setCount(int count) {
        this.count = count;
        // float x = mText.getX();
        // float y = mText.getY();
        // mText.setX(0);
        // mText.setY(0);
        if (count < 10) {
            mText.setText("0" + count);
        } else {
            mText.setText("" + count);
        }
        // mText.setX(x);
        // mText.setY(y);
    }

    public void setMission(int count, int type, Text mText) {

        this.count = count;
        this.type = type;
        this.mText = mText;
        attachChild(mText);
        // x = mText.getX();
        // y = mText.getY();
    }

}
